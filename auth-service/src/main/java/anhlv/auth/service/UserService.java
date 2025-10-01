package anhlv.auth.service;

import anhlv.auth.dto.UserLoginDTO;
import anhlv.auth.dto.UserRegisterDTO;
import anhlv.auth.dto.response.UserInfoRes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.company.common.apiresponse.CustomException;
import anhlv.auth.exception.ErrorMessages;
import anhlv.auth.model.UserModel;
import anhlv.auth.repository.UserRepository;
import anhlv.auth.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public UserInfoRes register(UserRegisterDTO userRegisterDTO) {

        // Kiểm tra nếu username đã tồn tại
        if (userRepository.findByUsername(userRegisterDTO.getUsername()) != null) {
            logger.warn("Register failed - username exists: username={}", userRegisterDTO.getUsername());
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        // Tạo đối tượng người dùng với mật khẩu thuần và lưu vào CSDL
        UserModel user = new UserModel(userRegisterDTO.getUsername(), userRegisterDTO.getPassword(), "user");
        userRepository.save(user);

        return UserInfoRes.builder()
                .username(user.getUsername()).role(user.getRole()).build();
    }

    public Map<String, String> Login(UserLoginDTO userLoginDTO) {

        UserModel user = userRepository.findByUsername(userLoginDTO.getUsername());

        if (user == null) {
            logger.warn("Login failed - user not found: username={}", userLoginDTO.getUsername());
            throw new CustomException(HttpStatus.NOT_FOUND.value(), ErrorMessages.USER_NOT_FOUND);
        }

        if (!userLoginDTO.getPassword().equals(user.getPassword())) {
            logger.warn("Login failed - invalid password: username={}", userLoginDTO.getUsername());
            throw new CustomException(HttpStatus.UNAUTHORIZED.value(), ErrorMessages.INVALID_PASSWORD);
        }

        // Tạo token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());

        return Map.of("token", token);
    }
    public Map<String, String> refreshToken(String username) {

        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("Refresh token failed - user not found: username={}", username);
            throw new CustomException(HttpStatus.NOT_FOUND.value(), ErrorMessages.USER_NOT_FOUND);
        }
        String newToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        return Map.of("token", newToken);
    }

}
