package com.ocj.security.service;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author L
* @description 针对表【video】的数据库操作Service
* @createDate 2023-10-28 09:11:25
*/
public interface VideoService extends IService<Video> {

    //上传视频
    ResponseResult addVideo(MultipartFile file);


    //获取视频列表


    //获取视频详细信息

}
