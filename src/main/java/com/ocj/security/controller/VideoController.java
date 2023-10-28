package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 未测试，ing
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    @Resource
    VideoService videoService;
    @PostMapping("/addVideo")
    public ResponseResult addVideo(MultipartFile file){

        return videoService.addVideo(file);
    }
}
