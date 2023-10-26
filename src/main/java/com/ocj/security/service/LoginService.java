package com.ocj.security.service;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.LoginRequest;
import com.ocj.security.domain.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginService {


    ResponseResult login(LoginRequest loginRequest);

    ResponseResult logout();
}
