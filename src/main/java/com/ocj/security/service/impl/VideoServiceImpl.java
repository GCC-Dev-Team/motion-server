package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.*;
import com.ocj.security.domain.vo.VideoCategoryVO;
import com.ocj.security.domain.vo.VideoCoverVO;
import com.ocj.security.domain.vo.VideoDataVO;
import com.ocj.security.domain.vo.VideoUserVO;
import com.ocj.security.mapper.CategoryMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoCoverService;
import com.ocj.security.service.VideoService;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
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
    VideoMapper videoMapper;
    @Resource
    FileService fileService;
    @Resource
    CommentService commentService;
    @Resource
    UserMapper userMapper;

    @Resource
    CategoryMapper categoryMapper;

    @Resource
    VideoCoverMapper videoCoverMapper;
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

        video.setCategoryId("4");
        video.setPublisher("98bf4c7f-fd20-4683-8323-f45ee72f0d1b");

        save(video);

        return ResponseResult.okResult(fileAddress);
    }

    @Override
    public VideoDataVO getVideoDataById(String videoId) {
        VideoDataVO videoDataVO = new VideoDataVO();


        Video video = videoMapper.selectById(videoId);
        BeanUtils.copyProperties(video,videoDataVO);

        String categoryId = video.getCategoryId();
        String publisherId = video.getPublisher();

        videoDataVO.setVideoCommentCount(commentService.getCommentCount(videoId));

        Category category = categoryMapper.selectById(categoryId);
        VideoCategoryVO videoCategoryVO = new VideoCategoryVO();
        BeanUtils.copyProperties(category,videoCategoryVO);
        videoDataVO.setVideoCategoryVO(videoCategoryVO);

        User user = userMapper.selectById(publisherId);
        VideoUserVO videoUserVO = new VideoUserVO();
        videoUserVO.setUserId(user.getId());
        videoUserVO.setUserName(user.getUserName());
        videoDataVO.setVideoUserVO(videoUserVO);

        //TODO 等下再搞封面
//        VideoCover videoCover = videoCoverMapper.selectById(videoId);
//        BeanUtils.copyProperties(videoCover,videoDataVO);

        VideoCoverVO videoCoverVO = new VideoCoverVO("http://s36fh9xu3.hn-bkt.clouddn.com/video/video%3A027e53eb4add4959.jpg", 100, 130);


        videoDataVO.setVideoCoverVO(videoCoverVO);
        return videoDataVO;
    }

    @Override
    public List<VideoDataVO> getVideoList() {
        List<String> lies = videoMapper.randVideoList();
        List<VideoDataVO> videoDataVOS = new ArrayList<>(); // 初始化为一个空的ArrayList
        for (String lie : lies) {
            VideoDataVO videoData = getVideoDataById(lie);
            videoDataVOS.add(videoData);
        }

        return videoDataVOS;

    }
}




