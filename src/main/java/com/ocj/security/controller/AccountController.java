package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.LoginRequest;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.service.LoginService;
import com.ocj.security.service.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Resource
    private LoginService loginService;
    @Resource
    UserService userService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginRequest loginRequest){

        return loginService.login(loginRequest);
    }

    @PostMapping("/register")
    ResponseResult register(@RequestBody RegisterRequest registerRequest){


        return userService.register(registerRequest);
    }

    @PostMapping("/logout")
    //@PreAuthorize("hasAuthority('test')")
    public ResponseResult logOut(){
        return loginService.logout();
    }

    @PostMapping("/hello")
    //@PreAuthorize("hasAuthority('test')")
    public String sayHello(){

        return "hello*** world";
    }

}