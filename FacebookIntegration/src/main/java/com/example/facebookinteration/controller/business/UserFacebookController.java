package com.example.facebookinteration.controller.business;

import com.example.facebookinteration.dto.response.GetUserFacebookResponseDTO;
import com.example.facebookinteration.service.business.UserFacebookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserFacebookController {

    @Autowired
    private UserFacebookService userFacebookService;

    // Endpoint để lấy danh sách người dùng dựa trên userId từ header
    @GetMapping("/user-facebook/me")
    public ResponseEntity<List<GetUserFacebookResponseDTO>> getMyUserFacebook(@RequestHeader("userId") Long userId) {
        // Gọi service để lấy danh sách người dùng
        List<GetUserFacebookResponseDTO> users = userFacebookService.getUsersByUserAppId(userId);
        return ResponseEntity.ok(users);
    }
}
