package anhlv.gateway.config;

import anhlv.gateway.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

@Configuration
public class GateWayConfig {

    // Đọc URI từ file cấu hình
    @Value("${gateway.routes.auth-service}")
    private String authServiceUri;

    @Value("${gateway.routes.face-service}")
    private String faceServiceUri;

    // Business service đã được merge vào face-service
    // @Value("${gateway.routes.business-service}")
    // private String businessServiceUri;

    // Khai báo Bean cho TraceIdResponseModifier để kích hoạt nó
    @Bean
    public TraceIdResponseModifier traceIdResponseModifier() {
        return new TraceIdResponseModifier();
    }

    // Cấu hình CORS toàn cục cho Gateway
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*")); // Sử dụng patterns để hỗ trợ tốt hơn
        corsConfig.setMaxAge(3600L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

    private static final String SECRET_KEY = "Levananh2005@";

    private static final Logger logger = LogManager.getLogger(GateWayConfig.class);

    // Lưu trữ các Bucket cho từng IP
    private final Map<String, Bucket> ipBuckets = new ConcurrentHashMap<>();

    // Cấu hình limit cho mỗi IP - Tăng lên 100 requests/phút để phù hợp với frontend
    private final Bandwidth ipLimit = Bandwidth.classic(100, Refill.intervally(100, Duration.ofMinutes(1)));

    @Bean
    public GlobalFilter timeoutFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            
            // Skip timeout cho WebSocket endpoints vì WebSocket connections cần tồn tại lâu dài
            if (path.startsWith("/api/face/ws/")) {
                return chain.filter(exchange);
            }
            
            return chain.filter(exchange)
                    .timeout(Duration.ofSeconds(10)) // Set timeout 10 giây
                    .onErrorResume(TimeoutException.class, ex -> {
                        logger.error("Request processing exceeded the timeout limit of 10 seconds.");
                        logger.error("Request timed out: Method = {}, Path = {}, Client IP = {}",
                                exchange.getRequest().getMethod(),
                                exchange.getRequest().getPath(),
                                exchange.getRequest().getRemoteAddress() != null
                                        ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
                                        : "Unknown");
                        throw new CustomException(504, "Yêu cầu đã quá thời gian xử lý cho phép.");
                    })
                    .onErrorResume(WebClientResponseException.NotFound.class, ex -> {
                        // Trường hợp path không tồn tại
                    logger.error("Requested resource not found: method={}, path={}",
                            exchange.getRequest().getMethod(), exchange.getRequest().getPath());
                        throw new CustomException(404, "Không tìm thấy tài nguyên yêu cầu.");
                    });
        };
    }

    // Global Filter để kiểm tra Rate Limiting và Authentication
    @Bean
    public GlobalFilter globalAuthenticationFilter() {
        return (exchange, chain) -> {
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().toString();
            logger.info("Request received - Method: {}, Path: {}}", method, path);

            // Bỏ qua các API không cần xác thực
            if (path.equals("/api/auth/login") || path.equals("/api/auth/register") || path.equals("/api/auth/redirect") || path.equals("/api/face/callback") || path.startsWith("/api/face/ws")) {
                return chain.filter(exchange);
            }

            // Kiểm tra rate limiting theo IP
            String ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            if (!isRateLimitedForIp(ipAddress)) {
                logger.error("Rate limit exceeded: ip={}, method={}, path={}", ipAddress, method, path);
                throw new CustomException(429, "Rate limit exceeded.");
            }

            // Kiểm tra JWT token
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String authToken = headers.getFirst(HttpHeaders.AUTHORIZATION);

            if (authToken == null) {
                logger.error("Authorization token missing: method={}, path={}", method, path);
                throw new CustomException(401, "Server không nhận được token");
            }

            if (!authToken.startsWith("Bearer ")) {
                logger.error("Authorization token invalid format: token={}, method={}, path={}", authToken, method, path);
                throw new CustomException(401, "Token không đúng định dạng. Vui lòng sử dụng định dạng 'Bearer <token>'");
            }

            String token = authToken.substring(7); // Lấy token sau "Bearer "
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(token)
                        .getBody();
            } catch (SignatureException e) {
                logger.error("Invalid token signature: token={}, method={}, path={}, error={}", authToken, method, path, e.getMessage());
                // Token không hợp lệ (signature không khớp)
                throw new CustomException(401, "Token không hợp lệ");
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // Token đã hết hạn
                logger.error("Expired token: token={}, method={}, path={}, error={}", authToken, method, path, e.getMessage());
                throw new CustomException(401, "Token đã hết hạn");
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                // Token bị hỏng
                logger.error("Malformed token: token={}, method={}, path={}, error={}", authToken, method, path, e.getMessage());
                throw new CustomException(401, "Token bị hỏng");
            } catch (Exception e) {
                // Lỗi tổng quát
                logger.error("Unexpected token decode error: token={}, method={}, path={}, error={}", authToken, method, path, e.getMessage());
                throw new CustomException(401, "Lỗi giải mã");
            }
            Long id = claims.get("id", Long.class); // Lấy id từ token
            String username = claims.get("username", String.class);
            String role = claims.get("role", String.class);

            if (id == null || !StringUtils.hasText(username) || !StringUtils.hasText(role)) {
                logger.error("Missing claims in token: token={}, method={}, path={}", authToken, method, path);
                throw new CustomException(401, "Token thiếu thông tin quan trọng");
            }

            // Thêm thông tin người dùng vào header request
            exchange = exchange.mutate().request(
                    exchange.getRequest()
                            .mutate()
                            .header("userId", id.toString())                 // Thêm ID
                            .header("username", username)
                            .header("role", role)
                            .build()
            ).build();

            return chain.filter(exchange);
        };
    }

    // Phương thức kiểm tra rate limiting theo IP
    private boolean isRateLimitedForIp(String ipAddress) {
        // Tạo một bucket mới cho IP nếu chưa có
        ipBuckets.putIfAbsent(ipAddress, createBucket());

        // Lấy bucket của IP từ cache
        Bucket bucket = ipBuckets.get(ipAddress);

        // Kiểm tra xem IP có vượt quá limit không
        return bucket.tryConsume(1); // Nếu không còn token trong bucket, trả về false
    }

    // Tạo Bucket với limit cho mỗi IP
    private Bucket createBucket() {
        return Bucket4j.builder()
                .addLimit(ipLimit)
                .build();
    }

    // Cấu hình các route cho Gateway
    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Route cho tất cả SockJS/WebSocket endpoints
                // Sử dụng HTTP URI, Gateway sẽ tự động upgrade WebSocket nếu cần
                .route("face-service-ws", r -> r
                        .path("/api/face/ws/**")
                        .filters(f -> f
                                .dedupeResponseHeader("Access-Control-Allow-Origin", "RETAIN_FIRST")
                                .dedupeResponseHeader("Access-Control-Allow-Credentials", "RETAIN_FIRST"))
                        .uri(faceServiceUri)) // http://localhost:8400 - Gateway tự handle WebSocket upgrade
                
                .route("auth-service", r -> r.path("/api/auth/**")
                        .filters(f -> f.cacheRequestBody(Object.class)) // Cache as Object để preserve body
                        .uri(authServiceUri))
                
                .route("face-service", r -> r.path("/api/face/**")
                        .filters(f -> f.cacheRequestBody(Object.class)) // Cache as Object để preserve body
                        .uri(faceServiceUri))
                
                // Business service đã được merge vào face-service
                .route("business-service", r -> r.path("/api/business/**")
                        .filters(f -> f.cacheRequestBody(Object.class)) // Cache as Object để preserve body
                        .uri(faceServiceUri))
                .build();
    }



}
