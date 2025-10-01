package com.example.facebookinteration.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.facebookinteration.constant.Constant;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            // Lấy giá trị userId từ header
            String userId = httpRequest.getHeader("userId");

            if (userId != null) {
                // Lưu userId vào ThreadContext
                ThreadContext.put(Constant.USER_ID_KEY, userId);
                logger.info("User ID added to ThreadContext: {}", userId);
            }
        }

        try {
            // Tiếp tục chuỗi lọc trước - để OpenTelemetry tạo span
            chain.doFilter(request, response);
        } finally {
            // Xóa ThreadContext sau khi request kết thúc
            ThreadContext.remove(Constant.USER_ID_KEY);
            // OpenTelemetry agent sẽ tự quản lý trace context cleanup
        }
    }

    @Override
    public void destroy() {
    }
}
