# API: Gửi Tin Nhắn Facebook Chủ Động

## 1. Mô tả chức năng

API cho phép chủ page gửi tin nhắn text chủ động đến khách hàng đã từng tương tác với page (đã inbox hoặc comment).

> **Lưu ý:** Facebook chỉ cho phép gửi tin nhắn chủ động trong vòng 24h kể từ lần cuối khách hàng tương tác với page.

---

## 2. Endpoint

```
POST /api/face/pages/{pageId}/send-text-message
```

- **{pageId}**: ID của page Facebook

---

## 3. Request

### Headers
- `Content-Type: application/json`

### Body
```json
{
  "userId": "string",    // PSID của khách hàng (Page-scoped ID)
  "message": "string"    // Nội dung tin nhắn muốn gửi
}
```

**Ví dụ:**
```json
{
  "userId": "24066995902989789",
  "message": "Xin chào, đây là tin nhắn từ page!"
}
```

---

## 4. Response

### Thành công (200 OK)
```json
{
  "recipient_id": "24066995902989789",
  "message_id": "m_5y3AiFXDJYLsxXm7C16crdj_1Yqg4jxVE0HLx_oABGrAMelxG--rI20IWOKOwGezxiWOE3oDTSminm0VoDfOLQ"
}
```

### Thất bại (ví dụ gửi ngoài 24h, 502 Bad Gateway)
```json
{
  "statusCode": 502,
  "message": "Facebook API error: Tin nhắn này được gửi ngoài khoảng thời gian cho phép 24h. Xem chính sách: https://developers.facebook.com/docs/messenger-platform/policy-overview",
  "timestamp": "2025-07-25T15:50:05.7456818"
}
```

---

## 5. Quy tắc & Lưu ý

- **userId** phải là PSID (Page-scoped ID) của khách hàng đã từng inbox hoặc tương tác với page.
- **Chỉ gửi được tin nhắn chủ động trong vòng 24h** kể từ lần cuối khách hàng tương tác với page.
- Nếu gửi ngoài 24h, API sẽ trả về lỗi và mô tả rõ ràng cho FE.
- Nếu muốn gửi ngoài 24h, cần sử dụng message tag hoặc template theo chính sách Facebook (liên hệ backend để mở rộng).

---

## 6. Test case mẫu (Karate)

```gherkin
Scenario: Happy case - send text message successfully
  Given url baseUrl + '/pages/' + pageId + '/send-text-message'
  And request { userId: '#(customerId)', message: 'Xin chao, day la tin nhan test!' }
  When method POST
  Then status 200
  And match response contains { recipient_id: '#notnull' }

Scenario: Error - send message outside 24h window
  Given url baseUrl + '/pages/' + pageId + '/send-text-message'
  And request { userId: '#(customerId)', message: 'Test ngoài 24h' }
  When method POST
  Then status 502
  And match response.message contains 'Tin nhắn này được gửi ngoài khoảng thời gian cho phép 24h'
```

---

## 7. Tham khảo

- [Facebook Messenger Platform Policy](https://developers.facebook.com/docs/messenger-platform/policy-overview)

---

**Nếu cần lấy PSID (userId) hoặc cần hỗ trợ về message tag, liên hệ backend.** 