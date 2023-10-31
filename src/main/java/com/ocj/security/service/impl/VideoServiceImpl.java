package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.PageRequest;
import com.ocj.security.domain.entity.*;
import com.ocj.security.domain.vo.*;
import com.ocj.security.mapper.CategoryMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.utils.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category, categoryVO);
        videoDataVO.setCategory(categoryVO);

        User user = userMapper.selectById(publisherId);
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getId());
        userVO.setUserName(user.getUserName());
        videoDataVO.setUser(userVO);

        //TODO 等下再搞封面
        VideoCover videoCover = videoCoverMapper.selectById(videoId);
        CoverVO coverVO = new CoverVO();
        BeanUtils.copyProperties(videoCover,coverVO);

//        CoverVO coverVO = new CoverVO("http://s36fh9xu3.hn-bkt.clouddn.com/video/video%3A027e53eb4add4959.jpg", 100, 130);


        videoDataVO.setCover(coverVO);
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

    @Override
    public PageVO getVideoList(PageRequest pageRequest) {

        Page<Video> videoPage = videoMapper.selectPage(new Page<>(pageRequest.getCurrentPage(), pageRequest.getPageSize()), null);

        List<Video> records = videoPage.getRecords();

        List<VideoDataVO> videoDataVOS = new ArrayList<>();


        for (Video video:records){

            VideoDataVO videoDataVO = new VideoDataVO();

            BeanUtils.copyProperties(video,videoDataVO);

            String categoryId = video.getCategoryId();
            String publisherId = video.getPublisher();

            videoDataVO.setVideoCommentCount(commentService.getCommentCount(video.getVideoId()));

            Category category = categoryMapper.selectById(categoryId);
            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category, categoryVO);
            videoDataVO.setCategory(categoryVO);

            User user = userMapper.selectById(publisherId);
            UserVO userVO = new UserVO();
            userVO.setUserId(user.getId());
            userVO.setUserName(user.getUserName());
            videoDataVO.setUser(userVO);

            //TODO 等下再搞封面
//        VideoCover videoCover = videoCoverMapper.selectById(videoId);
//        BeanUtils.copyProperties(videoCover,videoDataVO);

//            CoverVO coverVO = new CoverVO("http://s36fh9xu3.hn-bkt.clouddn.com/video/video%3A027e53eb4add4959.jpg", 100, 130);

            VideoCover videoCover = videoCoverMapper.selectById(video.getVideoId());
            CoverVO coverVO = new CoverVO();
            BeanUtils.copyProperties(videoCover,coverVO);

            videoDataVO.setCover(coverVO);

            videoDataVOS.add(videoDataVO);
        }





        return new PageVO(videoDataVOS,videoPage.getTotal()
                ,videoPage.getSize()
                ,videoPage.getCurrent());
    }
}




