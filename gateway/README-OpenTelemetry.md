# OpenTelemetry Integration cho Gateway Service

## 🎯 Tổng quan

Gateway Service đã được tích hợp OpenTelemetry để:
- **Structured Logging**: Log với trace context (traceId, spanId, serviceName)
- **Performance Monitoring**: Đo lường latency và throughput
- **Trace Context Propagation**: Truyền trace context qua các microservices

## 🔧 Cấu hình

### 1. OpenTelemetry Java Agent
- **Version**: 1.32.0 (stable)
- **Configuration**: `otel-agent.properties`
- **Service Name**: `gateway-service`

### 2. WebFlux Integration
- **Filter**: `OpenTelemetryFilter` (WebFilter cho reactive stack)
- **Order**: 1 (chạy đầu tiên)
- **MDC Injection**: Tự động inject trace context vào Log4j2

### 3. Logging Pattern
```
%d{yyyy-MM-dd HH:mm:ss.SSS} %highlight{%-5level}{STYLE=Logback} 
[%X{serviceName:-gateway-service}] [%X{traceId:-}] [%X{spanId:-}] 
[%thread] %logger{36} - %msg%n
```

## 🚀 Cách chạy (Giống Face Service)

### Prerequisites
1. **Java 17+**
2. **OpenTelemetry Java Agent** (tự động download như dependency)

### ⭐ Single Command (Giống Face Service)
```bash
# Lệnh duy nhất để build và chạy Gateway với OpenTelemetry (giống face service)
./gradlew runWithOtel

# Tương đương với face service command:
# ./mvnw clean generate-resources spring-boot:run
```

### Manual Build (nếu cần)
```bash
# Build project
./gradlew clean build

# Copy OpenTelemetry agent
./gradlew copyOtelAgent

# Run với agent
./gradlew runWithOtel
```

## 📊 Monitoring

### 1. Logs
- **File**: `logs/application.log`
- **JSON**: `logs/application.json`
- **Console**: Real-time với trace context

### 2. Log Format Example
```
2025-09-19 17:30:45.123 INFO [gateway-service] [a1b2c3d4e5f6g7h8] [i9j0k1l2m3n4o5p6] [reactor-http-nio-1] anhlv.gateway.filter.OpenTelemetryFilter - Request processed
```

## 🔄 Luồng hoạt động

```
1. Request đến Gateway → OpenTelemetry Agent tạo Span tự động
2. OpenTelemetryFilter.filter() → Inject trace context vào Log4j2 ThreadContext
3. Gateway logs → Hiển thị với trace context: [service] [traceId] [spanId]
4. Downstream requests → Thêm trace propagation headers (traceparent, X-Trace-Id, etc.)
5. Response → Cleanup context và log completion
```

## 🧪 Testing Distributed Tracing

### Quick Test
```bash
# Test basic request
curl -H "Accept: application/json" http://localhost:8080/actuator/health

# Test with custom trace headers
curl -H "traceparent: 00-test-trace-123-test-span-456-01" \
     http://localhost:8080/actuator/health

# Test multiple requests to verify trace isolation
for i in {1..3}; do curl -s http://localhost:8080/actuator/health > /dev/null & done
```

## 🔍 Monitoring & Debugging

### Real-time Log Monitoring
```bash
# Linux/Mac
tail -f logs/application.log | grep -E '\[gateway-service\]'

# Windows
powershell -Command "Get-Content logs\application.log -Wait | Select-String '\[gateway-service\]'"
```

### Expected Log Output
```
2025-09-19 17:30:45.123 INFO [gateway-service] [a1b2c3d4e5f6g7h8] [i9j0k1l2m3n4o5p6] [reactor-http-nio-1] anhlv.gateway.filter.OpenTelemetryFilter - Request [/actuator/health] - traceId: a1b2c3d4e5f6g7h8, spanId: i9j0k1l2m3n4o5p6
```

## 🌐 Distributed Tracing Features

### Context Propagation Headers
Gateway tự động thêm các headers sau cho downstream services:
- **W3C Trace Context**: `traceparent`, `tracestate` (chuẩn OpenTelemetry)
- **Custom Headers**: `X-Trace-Id`, `X-Span-Id`, `X-Service-Name` 
- **B3 Headers**: `X-B3-TraceId`, `X-B3-SpanId`, `X-B3-Sampled` (tương thích Zipkin)

### Reactive Stack Integration
- **WebFilter**: Hoạt động với Spring WebFlux reactive stack
- **Context Propagation**: Trace context được truyền qua reactive context
- **Thread Safety**: Đảm bảo trace isolation giữa các concurrent requests

## 📋 Configuration Comparison với Face Service

### Build & Run Command
```bash
# Face Service (Maven)
./mvnw clean generate-resources spring-boot:run

# Gateway Service (Gradle) - TƯƠNG TỰ
./gradlew runWithOtel
```

### OpenTelemetry Agent Setup
```xml
<!-- Face Service (pom.xml) -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-dependency-plugin</artifactId>
    <execution>
        <id>copy-otel-agent</id>
        <phase>generate-resources</phase>
        <goals><goal>copy</goal></goals>
    </execution>
</plugin>
```

```gradle
// Gateway Service (build.gradle) - TƯƠNG TỰ
configurations { otelAgent }
dependencies { 
    otelAgent 'io.opentelemetry.javaagent:opentelemetry-javaagent:1.32.0' 
}
task copyOtelAgent(type: Copy) {
    from configurations.otelAgent
    into 'build/libs'
}
```

### Trace Context Injection
```java
// Face Service (Servlet Stack)
// OpenTelemetryInterceptor.preHandle()
ThreadContext.put("traceId", spanContext.getTraceId());
ThreadContext.put("spanId", spanContext.getSpanId());
ThreadContext.put("serviceName", "facebook-integration-service");
```

```java
// Gateway Service (WebFlux Stack) - TƯƠNG TỰ LOGIC
// OpenTelemetryFilter.filter() 
return Mono.deferContextual(contextView -> {
    injectTraceContextToMDC(); // Inject vào ThreadContext GIỐNG face service
    // + thêm reactive context propagation
    // + thêm trace headers cho downstream
});
```

### Log Pattern (GIỐNG HỆT)
```xml
<!-- Cả Face Service và Gateway đều dùng pattern này -->
<Property name="LOG_PATTERN">
    %d{yyyy-MM-dd HH:mm:ss.SSS} %highlight{%-5level}{STYLE=Logback} 
    [%X{serviceName}] [%X{traceId}] [%X{spanId}] 
    [%thread] %logger{36} - %msg%n
</Property>
```

## ⚠️ Important Notes

1. **WebFlux vs Servlet**: Gateway dùng WebFlux nên cần approach khác với face service
2. **Thread Context**: Trong reactive stack, mỗi operation có thể chạy trên thread khác nhau
3. **Context Propagation**: Cần propagate context qua cả ThreadContext (logs) và reactive context
4. **Performance**: Tất cả exports bị tắt, chỉ dùng cho structured logging

## 🎯 Success Criteria

✅ **Single Command**: `./gradlew runWithOtel` giống face service `./mvnw clean generate-resources spring-boot:run`
✅ **Auto Dependency**: OpenTelemetry agent tự động download như dependency (không cần manual download)
✅ **Logs có đúng format**: `[gateway-service] [traceId] [spanId]` giống hệt face service
✅ **Trace isolation**: Mỗi request có traceId riêng biệt  
✅ **Context propagation**: Headers được thêm vào downstream requests cho distributed tracing
✅ **Performance**: Không ảnh hưởng đến latency của Gateway
✅ **Compatibility**: Tương thích 100% với face service trace format và workflow
1. Request đến Gateway
   ↓
2. OpenTelemetryFilter.filter()
   - Inject traceId, spanId, serviceName vào MDC
   ↓
3. Gateway xử lý request (routing, load balancing)
   ↓
4. Forward request đến downstream services
   ↓
5. Response trả về với trace context
   ↓
6. Cleanup MDC context
```

## 🛠️ Troubleshooting

### 1. Logs không có trace context
- Kiểm tra `OpenTelemetryFilter` được load
- Kiểm tra Log4j2 pattern có MDC keys
- Kiểm tra OpenTelemetry agent configuration

### 2. Performance issues
- Kiểm tra sampling rate trong `otel-agent.properties`
- Monitor memory usage của agent
- Tune buffer sizes nếu cần

## 📁 Files liên quan

- `otel-agent.properties` - OpenTelemetry agent configuration
- `OpenTelemetryFilter.java` - WebFilter cho trace injection
- `OpenTelemetryConfig.java` - OpenTelemetry beans
- `log4j2.xml` - Logging configuration với MDC
- `start-gateway-with-otel.sh/.bat` - Startup scripts

## 🔗 Liên kết

- [OpenTelemetry Java](https://opentelemetry.io/docs/instrumentation/java/)
- [Spring WebFlux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html)
