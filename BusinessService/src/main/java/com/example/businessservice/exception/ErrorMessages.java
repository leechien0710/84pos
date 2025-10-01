package com.example.businessservice.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {
    public static final String USER_NOT_FOUND = "Người dùng không tồn tại!";
    public static final String PHONE_ALREADY_EXISTS = "Số điện thoại đã được dùng cho tài khoản này rồi";
    public static final String ADDRESS_ALREADY_EXISTS = "Địa chỉ đã được dùng cho tài khoản này rồi";
}
