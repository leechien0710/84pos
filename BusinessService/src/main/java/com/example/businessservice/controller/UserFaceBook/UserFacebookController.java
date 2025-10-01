package com.example.businessservice.controller.UserFaceBook;

import com.example.businessservice.dto.response.GetUserFacebookResponseDTO;
import com.example.businessservice.service.UserFacebookService;
import com.example.businessservice.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constant.BASE_URL)
public class UserFacebookController {

    @Autowired
    private UserFacebookService userFacebookService;

    // Endpoint để lấy danh sách người dùng dựa trên userAppId
    @GetMapping("/user-facebook/{userAppId}")
    public ResponseEntity<List<GetUserFacebookResponseDTO>> getUsersByUserAppId(@PathVariable Long userAppId) {
        // Gọi service để lấy danh sách người dùng
        List<GetUserFacebookResponseDTO> users = userFacebookService.getUsersByUserAppId(userAppId);
        return ResponseEntity.ok(users);
    }
}
