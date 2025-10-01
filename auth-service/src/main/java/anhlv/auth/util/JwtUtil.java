package anhlv.auth.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class JwtUtil {

    private String secretKey = "Levananh2005@";

    // Hàm tạo token
    public String generateToken(Long id, String username, String role) {

        LocalDateTime now = LocalDateTime.now(ZoneOffset.ofHours(7));

        Date issuedAt = Date.from(now.atZone(ZoneOffset.ofHours(7)).toInstant());
        System.out.println(issuedAt);


        LocalDateTime expirationTime = now.plusHours(1);
        Date expirationDate = Date.from(expirationTime.atZone(ZoneOffset.ofHours(7)).toInstant());

        // Tạo và trả về token
        return Jwts.builder()
                .claim("username",username)  // Set subject là username
                .claim("role", role)   // Thêm claim cho role
                .claim("id", id)       // Thêm claim cho id
                .setIssuedAt(issuedAt)  // Thời gian tạo token
                .setExpiration(expirationDate)  // Thời gian hết hạn
                .signWith(SignatureAlgorithm.HS256, secretKey)  // Ký với secret key ban đầu
                .compact();  // Tạo token
    }
}
