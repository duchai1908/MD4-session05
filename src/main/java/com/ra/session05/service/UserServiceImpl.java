package com.ra.session05.service;

import com.ra.session05.model.dto.request.FormLogin;
import com.ra.session05.model.dto.request.FormRegister;
import com.ra.session05.model.dto.response.JWTResponse;
import com.ra.session05.model.entity.Role;
import com.ra.session05.model.entity.RoleName;
import com.ra.session05.model.entity.User;
import com.ra.session05.repository.IRoleRepository;
import com.ra.session05.repository.IUserRepository;
import com.ra.session05.security.jwt.JWTProvider;
import com.ra.session05.security.principle.UserDetailsCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JWTProvider jwtProvider;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public Boolean register(FormRegister formRegister) {
        User user = User.builder()
                .email(formRegister.getEmail())
                .fullName(formRegister.getFullName())
                .username(formRegister.getUsername())
                .password(passwordEncoder.encode(formRegister.getPassword()))
                .status(true)
                .build();
        if(formRegister.getRoles() != null && !formRegister.getRoles().isEmpty()){
          Set<Role> roles = new HashSet<>();
            formRegister.getRoles().forEach(
                    r->{
                        switch (r){
                            case "ADMIN":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_ADMIN).orElseThrow(()->new NoSuchElementException("Role Not Found")));
                            case "MANAGER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_MANAGER).orElseThrow(()->new NoSuchElementException("Role Not Found")));
                            case "USER":
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(()->new NoSuchElementException("Role Not Found")));
                            default:
                                roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(()->new NoSuchElementException("Role Not Found")));
                        }
                    }
            );
            user.setRoles(roles);
        }else{
            // mặc ịnh là user
            Set<Role> roles = new HashSet<>();
            roles.add(roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(()->new NoSuchElementException("Role Not Found")));
            user.setRoles(roles);
        }
        userRepository.save(user);
        return true;
    }

    @Override
    public JWTResponse login(FormLogin formLogin) {
        // xác thực username và password
        Authentication authentication = null;
        try{
            authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(formLogin.getUsername(), formLogin.getPassword()));
        }catch (AuthenticationException e){
            throw new  RuntimeException("username or password incorrect");
        }
        UserDetailsCustom detailsCustom = (UserDetailsCustom) authentication.getPrincipal();
        String accessToken = jwtProvider.generateAccessToken(detailsCustom);
        return JWTResponse.builder()
                .email(detailsCustom.getEmail())
                .fullName(detailsCustom.getFullName())
                .roleSet(detailsCustom.getAuthorities())
                .status(detailsCustom.getStatus())
                .accessToken(accessToken)
                .build();
    }
}
