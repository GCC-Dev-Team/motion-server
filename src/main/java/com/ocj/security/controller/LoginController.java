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
@RequestMapping("/User")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        return loginService.login(user);
    }

    @Resource
    UserService userService;

    @PostMapping("/register")
    ResponseResult register(@RequestBody RegisterRequest registerRequest){


        return userService.register(registerRequest);
    }

}
