package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.User;
import com.ocj.security.service.FileService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    FileService fileService;
    @PostMapping("/helloTest")
    public ResponseResult uploadFile(MultipartFile file){

        return fileService.uploadFile(file);
    }
    @GetMapping("/helloTest")
    public String uploadFile(){

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User principal = (User)authentication.getPrincipal();
////
////        principal.setPassword();
        return "jheiej i";
    }
}
