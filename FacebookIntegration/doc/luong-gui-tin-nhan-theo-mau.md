# Luồng Gửi Tin Nhắn Facebook Động Theo Mẫu

## 1. Mục đích
Cung cấp API cho phép gửi tin nhắn động (có thể chứa biến) tới khách hàng qua Facebook Messenger, sử dụng template do user cấu hình.

## 2. API Endpoint

- **URL:** `/api/face/pages/{pageId}/send-message`
- **Method:** `POST`
- **Content-Type:** `application/json`

### Request Body
```json
{
  "from": {
    "id": "<customerId>",
    "name": "<customerName>"
  },
  "message": "<nội dung tin nhắn ban đầu>"
}
```
- `from.id`: Facebook userId của khách hàng (recipient).
- `from.name`: Tên khách hàng (nếu có).
- `message`: Nội dung gốc (có thể dùng cho log hoặc fallback).

### Ví dụ request
```json
{
  "from": {
    "id": "24066995902989789",
    "name": "Chiến Lê"
  },
  "message": "Xin chào"
}
```

## 3. Xử lý backend
- Backend sẽ lấy template đã cấu hình cho pageId.
- Parse các biến trong template (ví dụ: `{{ten_khach}}`, `{{ten_shop}}`).
- Mapping giá trị biến động qua các resolver (ví dụ: lấy tên khách từ `from.name`, tên shop từ DB).
- Nếu có biến không xác định, trả về lỗi 400 với message rõ ràng.
- Nếu thiếu page, template, sender, trả về lỗi 404.
- Nếu gửi Facebook thất bại, trả về lỗi 502.
- Nếu thành công, gọi Facebook API gửi message tới user.

## 4. Response
### Thành công (200)
```json
{
  "recipient_id": "24066995902989789",
  "message_id": "mid.1234567890"
}
```

### Lỗi nghiệp vụ (ví dụ: thiếu page, template, sender, biến không hợp lệ)
```json
{
  "statusCode": 404,
  "message": "Page not found",
  "timestamp": "2024-07-24T15:00:02.3872751"
}
```
Hoặc:
```json
{
  "statusCode": 400,
  "message": "Biến '{{unknown_var}}' không được hệ thống hỗ trợ.",
  "timestamp": "2024-07-24T15:00:02.3872751"
}
```

### Lỗi Facebook API
```json
{
  "statusCode": 502,
  "message": "Facebook API error: ...",
  "timestamp": "2024-07-24T15:00:02.3872751"
}
```

## 5. Các biến động hỗ trợ trong template
- `{{ten_khach}}`: Tên khách hàng (lấy từ `from.name`)
- `{{ten_shop}}`: Tên page/shop (lấy từ DB theo pageId)

## 6. Quy tắc xử lý biến
- Nếu template chứa biến không hỗ trợ, API trả về lỗi 400, message rõ ràng.
- Nếu thiếu sender (page), trả về lỗi 404.
- Nếu thiếu template, trả về lỗi 404.
- Nếu thiếu page, trả về lỗi 404.

## 7. Lưu ý cho Frontend
- Luôn truyền đúng `pageId` và thông tin khách hàng.
- Xử lý các trường hợp lỗi dựa vào `statusCode` và `message` trong response.
- Có thể kiểm tra danh sách biến động hỗ trợ qua API `/api/face/system-variables` (GET).

## 8. Ví dụ luồng FE tích hợp
1. FE lấy danh sách page, chọn pageId.
2. FE lấy danh sách biến động nếu cần (GET `/api/face/system-variables`).
3. FE gửi request như trên tới `/api/face/pages/{pageId}/send-message` khi cần gửi tin nhắn.
4. FE xử lý response:
   - Nếu 200: Hiển thị thành công.
   - Nếu 400/404/502: Hiển thị message lỗi cho user.
