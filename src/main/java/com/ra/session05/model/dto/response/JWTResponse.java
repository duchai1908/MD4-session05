package com.ra.session05.model.dto.response;

import com.ra.session05.model.entity.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JWTResponse {
    private final String type = "Bearer"; // kiểu mã hoá
    private String accessToken; // có cho phép truy cập tài nguyên hay không
    private String fullName;
    private String email;
    private Collection<? extends GrantedAuthority> roleSet;
    private Boolean status;

}
