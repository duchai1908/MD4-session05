package com.ra.session05.service;

import com.ra.session05.model.dto.request.FormLogin;
import com.ra.session05.model.dto.request.FormRegister;
import com.ra.session05.model.dto.response.JWTResponse;

public interface IUserService {
    Boolean register(FormRegister formRegister);
    JWTResponse login(FormLogin formLogin);
}
