# API Ẩn/Hiện Comment - Facebook Integration

## Tổng quan
API này cho phép ẩn và hiện comment trong hệ thống Facebook Integration với tích hợp Facebook API thực tế.

## Base URL
```
http://localhost:8400/api/face
```

## Endpoints

### 1. Ẩn Comment
**PUT** `/comment/{commentId}/hide`

Ẩn một comment theo commentId trên cả Facebook và database local.

#### Parameters
- `commentId` (path): ID của comment cần ẩn
- `pageId` (query): ID của Facebook page (bắt buộc)

#### Request Example
```bash
curl -X PUT "http://localhost:8400/api/face/comment/123456789/hide?pageId=102261526228668"
```

#### Response
**Success (200):**
```json
{
    "success": true,
    "message": "Comment đã được ẩn thành công"
}
```

**Error (404):**
```json
{
    "success": false,
    "message": "Comment đã chọn không còn tồn tại"
}
```

### 2. Hiện Comment
**PUT** `/comment/{commentId}/show`

Hiện một comment đã bị ẩn theo commentId trên cả Facebook và database local.

#### Parameters
- `commentId` (path): ID của comment cần hiện
- `pageId` (query): ID của Facebook page (bắt buộc)

#### Request Example
```bash
curl -X PUT "http://localhost:8400/api/face/comment/123456789/show?pageId=102261526228668"
```

#### Response
**Success (200):**
```json
{
    "success": true,
    "message": "Comment đã được hiện thành công"
}
```

### 3. Ẩn Tất Cả Comment Của Post
**PUT** `/post/{postId}/comments/hide-all`

Ẩn tất cả comment của một bài post trên cả Facebook và database local.

#### Parameters
- `postId` (path): ID của post cần ẩn tất cả comment
- `pageId` (query): ID của Facebook page (bắt buộc)

#### Request Example
```bash
curl -X PUT "http://localhost:8400/api/face/post/987654321/comments/hide-all?pageId=102261526228668"
```

#### Response
**Success (200):**
```json
{
    "success": true,
    "message": "Đã ẩn 15 comment",
    "hiddenCount": 15,
    "postId": "987654321"
}
```

### 4. Hiện Tất Cả Comment Của Post
**PUT** `/post/{postId}/comments/show-all`

Hiện tất cả comment đã bị ẩn của một bài post trên cả Facebook và database local.

#### Parameters
- `postId` (path): ID của post cần hiện tất cả comment
- `pageId` (query): ID của Facebook page (bắt buộc)

#### Request Example
```bash
curl -X PUT "http://localhost:8400/api/face/post/987654321/comments/show-all?pageId=102261526228668"
```

#### Response
**Success (200):**
```json
{
    "success": true,
    "message": "Đã hiện 15 comment",
    "shownCount": 15,
    "postId": "987654321"
}
```

### 5. Lấy Danh Sách Comment
**GET** `/post/{postId}/comments`

Lấy danh sách comment của một post với phân trang và filter.

#### Parameters
- `postId` (path): ID của post
- `page` (query, optional): Số trang (mặc định: 0)
- `size` (query, optional): Số comment mỗi trang (mặc định: 20)
- `hasPhone` (query, optional): Filter comment có số điện thoại (true/false)
- `includeHidden` (query, optional): Bao gồm comment đã ẩn (mặc định: false)

#### Request Example
```bash
curl -X GET "http://localhost:8400/api/face/post/987654321/comments?page=0&size=10&hasPhone=true&includeHidden=false"
```

#### Response
**Success (200):**
```json
{
    "content": [
        {
            "id": "123456789",
            "message": "Comment text here",
            "postId": "987654321",
            "isHidden": false,
            "hasPhone": true,
            "createdTime": "2024-01-15T10:30:00"
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10
    },
    "totalElements": 25,
    "totalPages": 3
}
```

## Test Data cho Karate

### Page IDs có sẵn trong hệ thống:
```javascript
var testPages = {
    "page1": "102261526228668",
    "page2": "313176735202296", 
    "page3": "406454305878420"
}
```

### Sample Comment IDs (cần thay thế bằng ID thực tế):
```javascript
var testComments = {
    "comment1": "123456789012345",
    "comment2": "987654321098765",
    "comment3": "555666777888999"
}
```

### Sample Post IDs (cần thay thế bằng ID thực tế):
```javascript
var testPosts = {
    "post1": "111222333444555",
    "post2": "666777888999000",
    "post3": "123456789012345"
}
```

## Luồng Test Karate

### 1. Test Ẩn Comment Đơn Lẻ
```gherkin
Scenario: Ẩn một comment
Given url baseUrl + '/comment/' + commentId + '/hide'
And param pageId = pageId
When method PUT
Then status 200
And match response.success == true
And match response.message contains 'ẩn thành công'
```

### 2. Test Hiện Comment Đơn Lẻ
```gherkin
Scenario: Hiện một comment
Given url baseUrl + '/comment/' + commentId + '/show'
And param pageId = pageId
When method PUT
Then status 200
And match response.success == true
And match response.message contains 'hiện thành công'
```

### 3. Test Ẩn Tất Cả Comment
```gherkin
Scenario: Ẩn tất cả comment của post
Given url baseUrl + '/post/' + postId + '/comments/hide-all'
And param pageId = pageId
When method PUT
Then status 200
And match response.success == true
And match response.hiddenCount > 0
And match response.postId == postId
```

### 4. Test Hiện Tất Cả Comment
```gherkin
Scenario: Hiện tất cả comment của post
Given url baseUrl + '/post/' + postId + '/comments/show-all'
And param pageId = pageId
When method PUT
Then status 200
And match response.success == true
And match response.shownCount > 0
And match response.postId == postId
```

### 5. Test Lấy Danh Sách Comment
```gherkin
Scenario: Lấy danh sách comment
Given url baseUrl + '/post/' + postId + '/comments'
And param page = 0
And param size = 10
And param includeHidden = false
When method GET
Then status 200
And match response.content is array
And match response.totalElements >= 0
```

## Lưu ý quan trọng

1. **Facebook API Integration**: Tất cả API đều gọi Facebook API thực tế để ẩn/hiện comment
2. **Page Access Token**: Hệ thống tự động lấy access token từ database dựa trên pageId
3. **Batch Processing**: API ẩn/hiện tất cả comment sử dụng batch request để tối ưu hiệu năng
4. **Webhook Integration**: Comment mới từ webhook sẽ tự động ẩn nếu post đã được ẩn tất cả comment
5. **Database Sync**: Trạng thái ẩn/hiện được đồng bộ giữa Facebook và database local

## Error Handling

- **404**: Comment không tồn tại
- **500**: Lỗi Facebook API hoặc database
- **400**: Thiếu tham số bắt buộc (pageId) 