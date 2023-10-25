package com.ocj.security.service;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.User;

public interface LoginService {


    ResponseResult login(User user);

    ResponseResult logout();
}
