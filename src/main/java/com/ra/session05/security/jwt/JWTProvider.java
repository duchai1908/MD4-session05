package com.ra.session05.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JWTProvider {
    @Value("${jwt.expired-time}")
    private long expired;
    @Value("${jwt.secret-key}")
    private String secretKey;
    public String generateAccessToken(UserDetails userDetails) { // mã hoá thông tin của người dùng và ngày hết hạn
        Date today = new Date();
        return Jwts.builder().setSubject(userDetails.getUsername()) // Tiêu đề
                .setIssuedAt(today) // thời gian bắt đầu có hiệu lực của token
                .setExpiration(new Date(today.getTime()+expired))  // thời gian chết: 24h
                .signWith( SignatureAlgorithm.HS512,secretKey) // chữ ký, SignatureAlgo là thuật toán mã hoá
                .compact();
    }
    public Boolean validateToken(String token) { // xác thực token
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (ExpiredJwtException e){ // Lỗi JWT hết hạn
            log.error("JWT: message error expired:", e.getMessage());
        }catch (UnsupportedJwtException e){ // Lỗi không hỗ trợ JWT mình mã hoá
            log.error("JWT: message error unsupported:", e.getMessage());
        }catch (MalformedJwtException e){ // Lỗi JWT không hợp lệ, ví dụ copy chuỗi bậy bạ dán vào
            log.error("JWT: message error formated:", e.getMessage());
        }catch (SignatureException e){ // Lỗi chữ ký, ví dụ không đúng secret key hoặc thuật toán mã hoá
            log.error("JWT: message error signature not math:", e.getMessage());
        }catch (IllegalArgumentException e){ // Lỗi đối số truyền vào không hợp lệ
            log.error("JWT: message claims empty or argument invalid:", e.getMessage());
        }
        return false;
    }
    public String getUserNameFromToken(String token) { // giải mã token
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // giải mã token để lấy username từ tiêu đề
    }

}
