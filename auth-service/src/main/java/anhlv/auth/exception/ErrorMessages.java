package anhlv.auth.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorMessages {
    public static final String USERNAME_ALREADY_EXISTS = "Username đã tồn tại!";
    public static final String USER_NOT_FOUND = "Người dùng không tồn tại!";
    public static final String INVALID_PASSWORD = "Mật khẩu không chính xác!";
}
