# OpenTelemetry Trace Injection - Luồng hoạt động

## 🎯 Mục tiêu
Tự động inject `traceId`, `spanId`, `serviceName` vào **Log4j2 MDC** để hiển thị trong log pattern.

## 🔧 Components chính

### 1. **OpenTelemetry Java Agent** (1.32.0)
```bash
./mvnw clean generate-resources spring-boot:run -Dspring-boot.run.jvmArguments="-javaagent:C:\tools\opentelemetry-javaagent.jar -Dotel.javaagent.configuration-file=otel-agent.properties"
```

### 2. **otel-agent.properties**
```properties
otel.service.name=facebook-integration-service
otel.instrumentation.log4j-context-data.enabled=true
otel.instrumentation.servlet.enabled=true
otel.instrumentation.tomcat.enabled=true
```

### 3. **OpenTelemetryInterceptor** 
- **Vai trò**: Manual inject trace context vào Log4j2 ThreadContext
- **Timing**: `preHandle()` - trước khi controller chạy
- **Logic**:
```java
Span currentSpan = Span.current();
if (spanContext.isValid()) {
    ThreadContext.put("traceId", spanContext.getTraceId());
    ThreadContext.put("spanId", spanContext.getSpanId());
    ThreadContext.put("serviceName", "facebook-integration-service");
}
```

### 4. **Log4j2 Pattern**
```xml
<Property name="LOG_PATTERN">
    %d{yyyy-MM-dd HH:mm:ss.SSS} %highlight{%-5level}{STYLE=Logback} 
    [%X{serviceName}] [trace=%X{traceId}] [span=%X{spanId}] 
    [%thread] %logger{36} - %msg%n
</Property>
```

## 🔄 Luồng hoạt động

```
1. Request đến → OpenTelemetry Agent tạo Span
2. OpenTelemetryInterceptor.preHandle() → Manual inject vào ThreadContext
3. Controller/Service logs → Tự động có trace context
4. Log pattern hiển thị: [service] [trace=xxx] [span=xxx]
```

## ✅ Kết quả
```log
[facebook-integration-service] [trace=d8a342a2246bf5156b1c1ac82e452592] [span=c787aea1507bf91f] 
INFO com.example.controller.FaceController - Business logic executed
```

## 🎯 Key Points
- **Agent tạo span** → **Interceptor inject MDC** → **Log pattern hiển thị**
- **Servlet stack** (không dùng WebFlux để tránh conflict)
- **Version 1.32.0** (stable, không dùng 2.6.0-alpha)
- **Manual injection** vì auto-injection không hoạt động với Spring Boot 3.x + Log4j2

## 📁 Files liên quan
- `OpenTelemetryInterceptor.java` - Core injection logic
- `WebMvcConfig.java` - Đăng ký interceptor  
- `otel-agent.properties` - Agent configuration
- `log4j2-spring.xml` - Log pattern với MDC keys
