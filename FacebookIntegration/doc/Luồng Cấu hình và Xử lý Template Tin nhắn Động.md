# Tài liệu Kỹ thuật: Luồng Cấu hình và Xử lý Template Tin nhắn Động

**Mục tiêu:** Xây dựng một hệ thống cho phép người quản trị định nghĩa các "biến động", và người dùng (chủ shop) có thể sử dụng các biến này để tự do tạo mẫu tin nhắn trả lời tự động. Hệ thống phải linh hoạt để có thể thêm các loại biến mới trong tương lai mà không cần sửa đổi logic cốt lõi.

---

##  flowchart Luồng hoạt động tổng quan

1.  **Quản trị viên (Admin)**: Định nghĩa các biến hệ thống (ví dụ: `ten_khach`, `ma_don_hang`) và "công thức" lấy dữ liệu cho từng biến vào cơ sở dữ liệu.
2.  **Frontend (Giao diện người dùng)**: Gọi API để lấy danh sách các biến được hỗ trợ và hiển thị cho chủ shop dưới dạng các nút bấm hoặc gợi ý (ví dụ: khi gõ `@`).
3.  **Chủ shop**: Soạn thảo nội dung `template_content` bằng cách kết hợp văn bản tĩnh và các biến đã chọn.
4.  **Backend (Lưu mẫu)**: Khi chủ shop nhấn "Lưu", Backend nhận `template_content`, trích xuất các biến và **xác thực** chúng với danh sách biến hợp lệ trong DB. Nếu hợp lệ, lưu mẫu tin nhắn.
5.  **Hệ thống (Sự kiện)**: Một sự kiện xảy ra (ví dụ: có đơn hàng mới) và kích hoạt webhook đến Backend.
6.  **Backend (Xử lý và Gửi tin)**: Backend lấy mẫu tin, phân tích các biến, sử dụng "công thức" đã lưu trong DB và **Strategy Pattern** để thu thập dữ liệu tương ứng, sau đó điền vào mẫu và gửi tin nhắn cuối cùng cho khách hàng.

---

## 🏗️ Thiết kế Cơ sở dữ liệu

### Bảng 1: `System_Variables` (Thư viện biến và công thức)

Đây là bảng trung tâm, định nghĩa mọi biến mà hệ thống có thể xử lý.

| Tên cột           | Kiểu dữ liệu    | Mô tả                                                                                                        |
| ------------------ | --------------- | ------------------------------------------------------------------------------------------------------------ |
| `id` (PK)          | `INT`           | Khóa chính.                                                                                                  |
| `variable_key`     | `VARCHAR(100)`  | Khóa định danh duy nhất của biến (ví dụ: `ten_khach`, `ma_don_hang`). **UNIQUE**.                                |
| `display_name`     | `VARCHAR(255)`  | Tên thân thiện hiển thị cho người dùng (ví dụ: "Tên khách hàng").                                            |

### Bảng 2: `Page_Templates` (Mẫu tin của trang)

Lưu cấu hình mẫu tin của từng trang.

| Tên cột           | Kiểu dữ liệu    | Mô tả                                      |
| ------------------ | --------------- | ------------------------------------------ |
| `page_id` (PK)     | `VARCHAR(255)`  | Khóa chính. ID của Fanpage.                |
| `template_content` | `TEXT`          | Nội dung mẫu tin chứa các `variable_key`. |

---

## 🔌 Thiết kế API

### 1. API Lấy danh sách biến hệ thống

* **Mục đích**: Cung cấp cho FE danh sách các biến hợp lệ để hiển thị cho người dùng.
* **Method**: `GET`
* **Endpoint**: `/api/system-variables`
* **Response (200 OK)**:
    ```json
    [
        {
            "variable_key": "ten_khach",
            "display_name": "Tên khách hàng"
        },
        {
            "variable_key": "ten_shop",
            "display_name": "Tên shop"
        },
        {
            "variable_key": "ma_don_hang",
            "display_name": "Mã đơn hàng"
        }
    ]
    ```

### 2. API Cấu hình mẫu tin nhắn cho trang

* **Mục đích**: Tạo hoặc cập nhật mẫu tin nhắn cho một trang.
* **Method**: `PUT`
* **Endpoint**: `/api/pages/{page_id}/template`
* **Request Body**:
    ```json
    {
        "template_content": "Chào {{ten_khach}}, đơn hàng {{ma_don_hang}} của bạn đã được xác nhận."
    }
    ```
* **Response (200 OK)**:
    ```json
    {
        "page_id": "123456",
        "template_content": "Chào {{ten_khach}}, đơn hàng {{ma_don_hang}} của bạn đã được xác nhận."
    }
    ```
* **Response (400 Bad Request)**: Khi chứa biến không hợp lệ.
    ```json
    {
        "error": "Biến '{{ten_khach_sai}}' không được hệ thống hỗ trợ."
    }
    ```

---

## ⚙️ Luồng xử lý chi tiết phía Backend

### Luồng 1: Lưu Mẫu Tin (Validation Logic)

1.  **Nhận Request**: API `PUT /api/pages/{page_id}/template` nhận `template_content`.
2.  **Phân tích biến từ Request**: Dùng Regex `\\{\\{(.+?)\\}\\}`, trích xuất các `variable_key` từ `template_content` vào một `Set`.
    * *Ví dụ: `['ten_khach', 'ma_don_hang']`*
3.  **Lấy biến hợp lệ từ DB**: Thực hiện `SELECT variable_key FROM System_Variables` để có một `Set` các biến hợp lệ.
    * *Ví dụ: `['ten_khach', 'ten_shop', 'ma_don_hang', 'sdt_khach']`*
4.  **Xác thực**: Kiểm tra xem `Set` các biến từ request có phải là tập con của `Set` các biến hợp lệ từ DB hay không.
5.  **Phản hồi**:
    * Nếu không hợp lệ, trả về lỗi `400 Bad Request` với chi tiết biến sai.
    * Nếu hợp lệ, lưu vào bảng `Page_Templates` và trả về `200 OK`.
