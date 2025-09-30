# 84POS Deployment Guide

## Cấu trúc Project

```
84pos/
├── gateway/                 # API Gateway (port 1106)
├── auth-service/           # Authentication Service (port 5000)
├── FacebookIntegration/    # Facebook Integration Service (port 8400)
├── ShareLibrary/           # Shared Library
```

## Development Environment


### Chạy từng service riêng lẻ:

**Gateway:**
```bash
cd gateway
./gradlew runWithOtel
```

**Auth Service:**
```bash
cd auth-service
./gradlew runWithOtel
```

**Facebook Integration:**
```bash
cd FacebookIntegration
./mvnw spring-boot:run
```

## Production Environment

### Chạy từng service riêng lẻ với profile production:

**Gateway:**
```bash
cd gateway
./gradlew runWithOtel --args='--spring.profiles.active=prod'
```

**Auth Service:**
```bash
cd auth-service
./gradlew runWithOtel --args='--spring.profiles.active=prod'
```

**Facebook Integration:**
```bash
cd FacebookIntegration
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Endpoints

### Development URLs:
- **Gateway:** http://localhost:1106
- **Auth Service:** http://localhost:5000
- **Facebook Integration:** http://localhost:8400

### Production URLs:
- **Gateway:** http://13.54.216.34:1106
- **Auth Service:** http://13.54.216.34:5000
- **Facebook Integration:** http://13.54.216.34:8400

## Test Endpoints

### Auth Service (qua Gateway):
```bash
# Register
curl -X POST http://localhost:1106/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123","role":"USER"}'

# Login
curl -X POST http://localhost:1106/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"password123"}'

# Validate Token
curl -X GET http://localhost:1106/api/auth/validate \
  -H "Authorization: Bearer YOUR_TOKEN"

# User Info
curl -X GET http://localhost:1106/api/auth/userinfo \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Facebook Integration (qua Gateway):
```bash
# Health Check
curl -X GET http://localhost:1106/api/facebook/health
```

## Configuration

### Development:
- Sử dụng H2 database (embedded)
- Log level: DEBUG
- CORS: enabled for all origins

### Production:
- Sử dụng MySQL database
- Log level: INFO
- CORS: enabled for all origins
- JWT secret: configured in application-prod.properties

## Troubleshooting

### Kiểm tra services đang chạy:
```bash
ps aux | grep java
```

### Xem logs:
```bash
tail -f logs/*.log
```

### Kiểm tra port:
```bash
netstat -tlnp | grep :1106
netstat -tlnp | grep :5000
netstat -tlnp | grep :8400
```

### Reset database (development):
```bash
rm -rf data/
```

## Notes

- Tất cả services đều có OpenTelemetry tracing
- Logs được format với traceId và spanId
- Gateway route requests đến các services tương ứng
- CORS được cấu hình để cho phép tất cả origins
