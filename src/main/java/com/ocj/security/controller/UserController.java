package com.ocj.security.controller;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.service.UserService;
import com.ocj.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private QiniuApiService qiniuApiService;

    @Resource
    private UserService userService;

    /**
     * 上传头像
     */
    @PostMapping("/uploadFile")
    public ResponseResult uploadFile(MultipartFile file){
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("文件名:{}",originalFilename);
        String avatarURL = qiniuApiService.uploadFile(file, "avatar/"+originalFilename);
        userService.setAvatar(SecurityUtils.getUserId(),avatarURL);

        return ResponseResult.okResult();
    }


}
