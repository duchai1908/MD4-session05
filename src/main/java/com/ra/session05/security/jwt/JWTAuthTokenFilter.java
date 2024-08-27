package com.ra.session05.security.jwt;

import com.ra.session05.security.principle.UserDetailsServiceCustom;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTAuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private UserDetailsServiceCustom userDetailsServiceCustom;
    @Override
    // Chặn yêu cầu của người dùng liên quan đến việc truy xuất tài nguyên thông qua token
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // xử lý token và giải mã -> cấp quyền / chứng nhận và lưu vào SecurityContext
        // lấy token
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            String username = jwtProvider.getUserNameFromToken(token);
            UserDetails userDetails = userDetailsServiceCustom.loadUserByUsername(username);
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            // lưu Authentication vào security Context thông qua securityContextHolder
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        // cho request và response tiếp tục đi tiếp vào các tầng khác
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        // lấy ra chuỗi bearer token
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
