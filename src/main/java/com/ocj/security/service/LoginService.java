package com.ocj.security.service;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.LoginRequest;
import com.ocj.security.domain.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface LoginService {

    /**
     * 登录
     * @param loginRequest
     * @return
     */
    ResponseResult login(LoginRequest loginRequest);

    /**
     * 登出
     * @return
     */
    ResponseResult logout();
}
