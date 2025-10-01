package anhlv.auth.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class ControllerBodyLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerBodyLoggingAspect.class);
    private static final int MAX_LOG_BODY_LENGTH = 2048; // 2KB

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("within(anhlv.auth.controller..*)")
    public Object logRequestBodyBeforeController(ProceedingJoinPoint pjp) throws Throwable {
        try {
            ServletRequestAttributes attrs = currentRequestAttributes();
            HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

            // Log START và REQ HEADER ngay trước khi log REQ BODY (trace đã được set bởi Interceptor)
            if (request != null) {
                logger.info("=========================================");
                logger.info("START {} {}", request.getMethod(), request.getRequestURI());
            }

            Object requestBodyObject = extractRequestBodyArgument(pjp);
            if (requestBodyObject != null) {
                String json = safeToJson(requestBodyObject);
                json = sanitize(json);
                if (json != null && json.length() > MAX_LOG_BODY_LENGTH) {
                    json = json.substring(0, MAX_LOG_BODY_LENGTH) + "...<truncated>";
                }
                if (request != null) {
                    logger.info("REQ BODY (AOP) {} {}: {}", request.getMethod(), request.getRequestURI(), json);
                } else {
                    logger.info("REQ BODY (AOP): {}", json);
                }
            }
        } catch (Exception ignored) {
            // Do not break request flow if logging fails
        }

        return pjp.proceed();
    }

    private ServletRequestAttributes currentRequestAttributes() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes) {
            return (ServletRequestAttributes) attrs;
        }
        return null;
    }

    private Object extractRequestBodyArgument(ProceedingJoinPoint pjp) {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Object[] args = pjp.getArgs();

        // Ưu tiên tham số có @RequestBody
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation ann : parameterAnnotations[i]) {
                if (ann instanceof RequestBody) {
                    return args[i];
                }
            }
        }

        // Fallback: chọn đối tượng DTO đầu tiên không phải Servlet/BindingResult
        for (Object arg : args) {
            if (arg == null) continue;
            String cn = arg.getClass().getName();
            if (cn.startsWith("jakarta.servlet.") || cn.startsWith("org.springframework.validation.")) {
                continue;
            }
            return arg;
        }
        return null;
    }

    private String safeToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return String.valueOf(obj);
        }
    }

    private String sanitize(String json) {
        if (json == null) return null;
        String result = json;
        result = result.replaceAll("\\\"password\\\"\\s*:\\s*\\\".*?\\\"", "\\\"password\\\":\\\"***\\\"");
        result = result.replaceAll("\\\"token\\\"\\s*:\\s*\\\".*?\\\"", "\\\"token\\\":\\\"***\\\"");
        result = result.replaceAll("\\\"authorization\\\"\\s*:\\s*\\\".*?\\\"", "\\\"authorization\\\":\\\"***\\\"");
        return result;
    }
}


