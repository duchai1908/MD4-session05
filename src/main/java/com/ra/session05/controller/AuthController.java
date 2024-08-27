package com.ra.session05.controller;

import com.ra.session05.model.dto.request.FormLogin;
import com.ra.session05.model.dto.request.FormRegister;
import com.ra.session05.model.dto.response.JWTResponse;
import com.ra.session05.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api.example.com/v1/auth")
public class AuthController {
    @Autowired
    private IUserService userService;
    @PostMapping("/sign-in")
    public ResponseEntity<JWTResponse> doLogin(@RequestBody FormLogin formLogin){
        return new ResponseEntity<>(userService.login(formLogin), HttpStatus.OK);
    }
    @PostMapping("/sign-up")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Method danh rieng cho admin
    public ResponseEntity<?> doRegister(@RequestBody FormRegister formRegister){
        boolean check = userService.register(formRegister);
        if(check){
            Map<String,String> map = new HashMap<>();
            map.put("message","Account created successfully");
            return new ResponseEntity<>(map,HttpStatus.CREATED);
        }else{
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
