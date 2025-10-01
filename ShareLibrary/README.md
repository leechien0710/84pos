# ShareLibrary - Internal Library cho Microservices

## Tổng quan

ShareLibrary là thư viện nội bộ được thiết kế để chuẩn hóa giao tiếp giữa các microservice trong hệ thống. Thư viện cung cấp:

- ✅ **API Response Envelope**: Format response thống nhất cho tất cả services
- ✅ **Distributed Tracing**: Tích hợp hoàn chỉnh với OpenTelemetry và W3C Trace Context
- ✅ **Auto-instrumentation**: Tự động trace HTTP, DB, Kafka, RabbitMQ
- ✅ **Logging Integration**: Tự động thêm traceId/spanId vào logs
- ✅ **Exception Handling**: Xử lý lỗi chuẩn hóa
- ✅ **Entity Classes**: Các entity chung cho Facebook integration

## Cài đặt

### 1. Thêm dependency vào microservice

```xml
<dependency>
    <groupId>com.company</groupId>
    <artifactId>ShareLibrary</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 2. Cấu hình cơ bản

```yaml
# application.yml
spring:
  config:
    import: classpath:application-tracing.yml
  profiles:
    active: tracing

# Override service information
tracing:
  service-name: your-service-name
  service-version: 1.0.0
  environment: production
```

### 3. Cấu hình logging

**Cho Logback:**
```yaml
logging:
  config: classpath:logback-spring-tracing.xml
```

**Cho Log4j2:**
```yaml
logging:
  config: classpath:log4j2-tracing.xml
```

## Tính năng chính

### 1. API Response Envelope

Tất cả API responses đều có format thống nhất:

```java
@RestController
public class UserController {
    
    @GetMapping("/users/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ApiResponseBuilder.success(user);
    }
}
```

**Response format:**
```json
{
  "httpStatus": 200,
  "code": "00",
  "codeFull": "SUCCESS-00",
  "message": "Success",
  "data": { /* Dữ liệu trả về */ },
  "processingTime": 150,
  "timestamp": "2024-01-15T10:30:00",
  "requestId": "req-12345"
}
```

### 2. Distributed Tracing

Tự động trace tất cả operations:

```java
@Service
public class UserService {
    
    @Autowired
    private TracingUtils tracingUtils;
    
    public User findById(Long id) {
        return tracingUtils.traceOperation("user.findById", () -> {
            // Database operation được tự động trace
            return userRepository.findById(id);
        });
    }
}
```

**Logs với traceId:**
```
2024-01-15 10:30:00.123 [http-nio-8080-exec-1] INFO  [abc123def456,xyz789] c.c.u.UserService - Tìm kiếm user với ID: 123
```

### 3. Auto-instrumentation

Các operations sau được tự động instrument:

- ✅ **HTTP Requests**: Tất cả REST endpoints
- ✅ **Database Operations**: JDBC, JPA, Hibernate
- ✅ **Kafka**: Producers và Consumers
- ✅ **RabbitMQ**: Message sending và receiving
- ✅ **External Services**: HTTP client calls

### 4. W3C Trace Context

Tự động propagate trace context giữa services:

```java
// Service A gọi Service B
@Autowired
private RestTemplate restTemplate;

public void callServiceB() {
    // Trace headers được tự động thêm vào request
    String response = restTemplate.getForObject("http://service-b/api/data", String.class);
}
```

## Cấu hình chi tiết

### 1. Tracing Configuration

```yaml
tracing:
  enabled: true
  service-name: user-service
  service-version: 1.0.0
  environment: production
  
  sampling:
    type: TRACE_ID_RATIO_BASED
    ratio: 0.1  # 10% sampling cho production
  
  exporters:
    otlp:
      enabled: true
      endpoint: http://jaeger-collector:4317
```

### 2. Logging Configuration

```yaml
tracing:
  logging:
    enabled: true
    trace-id-field: traceId
    span-id-field: spanId
    service-name-field: serviceName
```

## Sử dụng nâng cao

### 1. Manual Tracing

```java
@Service
public class ComplexService {
    
    @Autowired
    private TracingUtils tracingUtils;
    
    public void processData() {
        tracingUtils.traceOperation("data.process", () -> {
            // Thêm attributes
            tracingUtils.addAttribute("data.size", data.size());
            tracingUtils.addAttribute("data.type", "user_data");
            
            // Thêm events
            tracingUtils.addEvent("processing.started", "Bắt đầu xử lý data");
            
            // Business logic
            doProcessData();
            
            tracingUtils.addEvent("processing.completed", "Hoàn thành xử lý data");
        });
    }
}
```

### 2. Database Tracing

```java
@Repository
public class UserRepository {
    
    @Autowired
    private TracingUtils tracingUtils;
    
    public User findById(Long id) {
        return tracingUtils.traceDatabaseOperation("SELECT users", () -> {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", User.class, id);
        });
    }
}
```

### 3. External Service Tracing

```java
@Service
public class ExternalService {
    
    @Autowired
    private TracingUtils tracingUtils;
    
    public String callExternalAPI() {
        return tracingUtils.traceExternalService("payment-service", "processPayment", () -> {
            return restTemplate.getForObject("http://payment-service/api/process", String.class);
        });
    }
}
```

### 4. Message Processing Tracing

```java
@Component
public class MessageHandler {
    
    @Autowired
    private TracingUtils tracingUtils;
    
    @KafkaListener(topics = "user-events")
    public void handleMessage(UserEvent event) {
        tracingUtils.traceMessageProcessing("kafka", "user.event.process", () -> {
            // Process message
            processEvent(event);
        });
    }
}
```

## Monitoring và Debugging

### 1. Health Check

```bash
curl http://localhost:8080/actuator/health
```

### 2. Metrics

```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/prometheus
```

### 3. Trace Information

```java
@Autowired
private TracingUtils tracingUtils;

public void logTraceInfo() {
    String traceId = tracingUtils.getCurrentTraceId();
    String spanId = tracingUtils.getCurrentSpanId();
    log.info("Current trace: {} - {}", traceId, spanId);
}
```

## Tài liệu chi tiết

- [API Response Guide](API_RESPONSE_GUIDE.md) - Hướng dẫn sử dụng API Response
- [Distributed Tracing Guide](TRACING_GUIDE.md) - Hướng dẫn chi tiết về tracing
- [Postman Testing](POSTMAN_TESTING.md) - Hướng dẫn test API

## Cấu trúc thư mục

```
src/main/java/com/company/common/
├── apiresponse/          # API Response classes
├── entity/              # Entity classes
└── tracing/             # Distributed tracing
    ├── config/          # Configuration classes
    ├── example/         # Usage examples
    └── *.java           # Core tracing classes
```

## Yêu cầu hệ thống

- Java 17+
- Spring Boot 3.2+
- Maven 3.6+

## Dependencies chính

- Spring Boot Web Starter
- Spring Boot Actuator
- OpenTelemetry SDK
- Micrometer
- Jackson
- Lombok

## Troubleshooting

### 1. Không có traceId trong logs

- Kiểm tra `tracing.enabled=true`
- Kiểm tra logging configuration
- Kiểm tra MDC filter

### 2. Traces không được gửi đi

- Kiểm tra exporter configuration
- Kiểm tra network connectivity
- Kiểm tra sampling ratio

### 3. Performance issues

- Giảm sampling ratio
- Kiểm tra batch size
- Monitor memory usage

## Best Practices

1. **Service Naming**: Sử dụng tên service rõ ràng
2. **Sampling**: Phù hợp với môi trường (dev: 100%, prod: 1-10%)
3. **Attributes**: Thêm attributes có ý nghĩa
4. **Error Handling**: Luôn record exceptions
5. **Logging**: Sử dụng structured logging
6. **Monitoring**: Monitor trace volume

## Support

Nếu gặp vấn đề, hãy kiểm tra:
- Logs của ShareLibrary components
- OpenTelemetry logs
- Network connectivity
- Configuration files

## Changelog

### v1.0.0
- ✅ API Response Envelope
- ✅ Distributed Tracing với OpenTelemetry
- ✅ Auto-instrumentation cho HTTP, DB, Kafka, RabbitMQ
- ✅ W3C Trace Context propagation
- ✅ Logging integration với traceId/spanId
- ✅ Example code và documentation
