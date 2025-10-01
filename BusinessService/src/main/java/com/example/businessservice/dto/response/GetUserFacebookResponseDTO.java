package com.example.businessservice.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetUserFacebookResponseDTO {

    @NotNull
    private String userId; // user_id lấy từ Facebook, đổi thành camelCase

    @NotNull
    private String name; // Tên người dùng từ Facebook

    @NotNull
    private String avatar; // URL avatar từ Facebook

    @NotNull
    private Long userAppId; // ID ứng dụng (long), đổi thành camelCase

    @NotNull
    private Long expiresAt; // Thời điểm hết hạn token, đổi thành camelCase
}
