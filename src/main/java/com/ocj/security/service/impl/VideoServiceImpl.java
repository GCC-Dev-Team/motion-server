package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.LoginUser;
import com.ocj.security.domain.entity.Video;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.SecurityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.Random;
import java.util.UUID;

import static com.ocj.security.utils.SecurityUtils.getLoginUser;

/**
* @author L
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-10-28 09:11:25
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{
    @Resource
    FileService fileService;
    @Override
    public ResponseResult addVideo(MultipartFile file) {


        String videoId = "video:"+ RandomUtil.generateRandomString(16);

        String fileAddress = fileService.uploadFile(file, "video/" + videoId);

        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;

        Video video = new Video();
        video.setVideoId(videoId);
        video.setUrl(fileAddress);
        video.setDescription("测试视频");
        video.setStatus(1);
        video.setTags("#测试,最新标签，这个不是分类");
        video.setViews(0L);
        video.setLikeCount(0L);

        video.setCategoryId(String.valueOf(randomNumber));
        video.setPublisher(SecurityUtils.getLoginUser().getUser().getId());

        save(video);

        return ResponseResult.okResult(fileAddress);
    }
}




