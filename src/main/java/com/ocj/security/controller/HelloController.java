package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@RestController
/**
 * 测试使用前端不需要看
 */
public class HelloController {

    @Resource
    FileService fileService;
    @PostMapping("/hello")
    //@PreAuthorize("hasAuthority('test')")
    public String sayHello(){

        return "hello world";
    }



}
