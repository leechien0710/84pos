package anhlv.auth.controller;

import anhlv.auth.dto.UserLoginDTO;
import anhlv.auth.dto.UserRegisterDTO;
import anhlv.auth.dto.response.UserInfoRes;
import anhlv.auth.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    // Đăng ký người dùng
    @PostMapping("/register")
    public UserInfoRes register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return userService.register(userRegisterDTO);
    }

    // Đăng nhập người dùng
    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return userService.Login(userLoginDTO);
    }

    @PostMapping("/refresh-token")
    public Map<String, String> refreshToken(@RequestHeader("username") String username) {
        return userService.refreshToken(username);
    }


}
