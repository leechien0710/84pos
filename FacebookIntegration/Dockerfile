# Sử dụng một JDK base image nhỏ gọn và phổ biến
FROM eclipse-temurin:17-jdk-alpine AS builder

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file cấu hình Maven và tải các dependencies trước để tận dụng layer caching
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Sao chép mã nguồn và build ứng dụng
COPY src src
RUN ./mvnw clean package -DskipTests

# Giai đoạn runtime với JRE nhỏ gọn hơn
FROM eclipse-temurin:17-jre-alpine

# Thiết lập thư mục làm việc
WORKDIR /app

# Sao chép file JAR đã build từ giai đoạn builder
COPY --from=builder /app/target/FacebookInteration-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình tham số JVM để tối ưu hiệu năng
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Lệnh chạy ứng dụng
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
