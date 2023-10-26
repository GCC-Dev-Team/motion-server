package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.domain.entity.User;
import com.ocj.security.service.LoginService;
import com.ocj.security.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/Account")
public class LoginController {

    @Resource
    private LoginService loginService;
    @Resource
    UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @PostMapping("/register")
    ResponseResult register(@RequestBody RegisterRequest registerRequest){


        return userService.register(registerRequest);
    }

}
