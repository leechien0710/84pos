# FacebookIntegration

## 1. Mô tả ngắn
Dự án FacebookIntegration là một ứng dụng backend Spring Boot giúp đồng bộ, quản lý và xử lý dữ liệu từ Facebook Page (bài viết, bình luận, livestream, hội thoại, v.v.) thông qua API Facebook Graph và Webhook. Hệ thống hỗ trợ lưu trữ, phân tích, đồng bộ dữ liệu và cung cấp các API phục vụ cho các hệ thống quản trị, chăm sóc khách hàng đa kênh.

## 2. Tính năng chính
- **Đồng bộ Page, Post, Video, Livestream**: Lấy và lưu trữ thông tin page, bài viết, video, livestream từ Facebook.
- **Quản lý bình luận, hội thoại**: Lưu trữ, truy vấn bình luận, hội thoại, tin nhắn từ các bài viết/page.
- **Webhook Facebook**: Nhận và xử lý realtime các sự kiện từ Facebook (bình luận mới, livestream, v.v.).
- **Kích hoạt/ngưng kích hoạt Page**: API để bật/tắt trạng thái hoạt động của page.
- **Đồng bộ dữ liệu hàng loạt**: Hỗ trợ đồng bộ toàn bộ dữ liệu page, post, video theo yêu cầu.
- **Tích hợp WebSocket**: Đẩy realtime sự kiện mới (bình luận, tin nhắn) tới client.
- **Bảo mật**: Tích hợp Spring Security cho các endpoint nhạy cảm.

## 3. Công nghệ sử dụng
- **Spring Boot 3.3.2** (WebFlux, Security, Data JPA, WebSocket, AOP)
- **MySQL** (hoặc H2 cho runtime test)
- **RestFB** (Java Facebook API client)
- **Log4j2** (logging)
- **Lombok** (giảm boilerplate code)
- **Disruptor** (event processing)
- **Maven** (quản lý phụ thuộc)
- **Reactor** (lập trình bất đồng bộ)
- **Docker** (hỗ trợ build/run container)

## 4. Hướng dẫn chạy dự án

### Yêu cầu
- Java 17+
- Maven 3.6+

### Build & Run
```bash
# Build project
./mvnw clean install

# Chạy ứng dụng (mặc định port 8400)
./mvnw spring-boot:run

# Hoặc build Docker image
docker build -t facebook-integration .
docker run -p 8400:8400 facebook-integration
```

## 5. API endpoints chính

| Method | Endpoint                                 | Mô tả ngắn                                      |
|--------|------------------------------------------|-------------------------------------------------|
| GET    | /api/user/{userId}/pages                 | Lấy danh sách page của user                      |
| GET    | /api/user/{userId}/pages/active          | Lấy danh sách page đang active                   |
| POST   | /api/pages/active                        | Kích hoạt page                                   |
| POST   | /api/pages/inactive                      | Ngưng kích hoạt page                             |
| POST   | /api/pages/sync-all                      | Đồng bộ toàn bộ dữ liệu page (post, video)       |
| GET    | /api/page/{pageId}/posts                 | Lấy danh sách bài viết của page                  |
| GET    | /api/post/{postId}/comments              | Lấy bình luận của bài viết                       |
| GET    | /api/page/{id}/conversations             | Lấy danh sách hội thoại của page                 |
| GET    | /api/page/{pageId}/conversation/{conversationId}/messages | Lấy tin nhắn của hội thoại           |
| GET    | /api/page/{pageId}/live/{liveId}         | Lấy thông tin livestream                         |
| GET    | /api/auth-url                            | Lấy URL xác thực Facebook                        |
| GET    | /api/callback                            | Callback xác thực Facebook                       |
| GET    | /webhook                                 | Xác thực webhook Facebook                        |
| POST   | /webhook                                 | Nhận sự kiện realtime từ Facebook                |

> **Lưu ý:** Tất cả các endpoint đều prefix `/api` (theo Constant.BASE_URL).

## 6. Hướng dẫn cấu hình

Cấu hình chính nằm ở `src/main/resources/application.properties`:

```properties
server.port=8400

# Kết nối MySQL
spring.datasource.url=jdbc:mysql://<host>:<port>/<db>
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.jdbc.batch_size=100
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Logging
logging.log4j.context-selector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector
```

> Có thể cấu hình SSL, H2, MariaDB, ... bằng cách bỏ comment các dòng tương ứng.

## 7. Hướng dẫn phát triển thêm

- **Chạy môi trường dev**: Sử dụng H2 hoặc MariaDB (cấu hình trong `application.properties`).
- **Chỉnh sửa code**: Source code chính nằm ở `src/main/java/com/example/facebookinteration/`.
- **Thêm API mới**: Tạo controller/service/repository mới theo chuẩn Spring Boot.
- **Test**: Thêm test tại `src/test/java/com/example/facebookinteration/`.
- **Hot reload**: Có thể tích hợp Spring DevTools để reload nhanh khi dev.