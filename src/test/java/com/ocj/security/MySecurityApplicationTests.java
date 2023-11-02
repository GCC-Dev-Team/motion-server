package com.ocj.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.entity.Video;
import com.ocj.security.domain.entity.VideoCover;
import com.ocj.security.domain.vo.CoverVO;
import com.ocj.security.domain.vo.VideoDataVO;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.service.FileService;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.service.VideoService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.FileToMultipartFileConverter;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@SpringBootTest
class MySecurityApplicationTests {


    @Resource
    VideoMapper videoMapper;
    @Resource
    VideoCoverMapper videoCoverMapper;

    @Test
    void updateData(){
//
//        List<Video> videos = videoMapper.selectList(null);
//
//        for (Video video:videos){
//
//            Video video1 = new Video();
//
//            BeanUtils.copyProperties(video,video1);
////            System.out.println();
//            video1.setVideoId("video"+video.getVideoId().substring(6));
//
//            VideoCover videoCover = new VideoCover();
//            VideoCover videoCover1 = videoCoverMapper.selectById(video.getVideoId());
//
//            BeanUtils.copyProperties(videoCover1,videoCover);
//            videoCover.setVideoId("video"+video.getVideoId().substring(6));
//
//            videoCoverMapper.insert(videoCover);
//            videoCoverMapper.deleteById(videoCover1);
//
//            videoMapper.insert(video1);
//            videoMapper.deleteById(video);
//
//            System.out.println("ok");
//        }
    }

}
