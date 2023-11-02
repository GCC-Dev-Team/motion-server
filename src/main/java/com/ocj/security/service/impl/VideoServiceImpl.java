package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import java.util.stream.Collectors;

/**
 * @author L
 * @description 针对表【video】的数据库操作Service实现
 * @createDate 2023-10-28 09:11:25
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
        implements VideoService {

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
    public ResponseResult addVideo(MultipartFile file) {


        String videoId = "video" + RandomUtil.generateRandomString(16);

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

    public VideoDataVO videoToVideoDataVO(Video video){

        VideoDataVO videoDataVO = new VideoDataVO();

        BeanUtils.copyProperties(video, videoDataVO);

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
        videoDataVO.setCreator(userVO);

        VideoCover videoCover = videoCoverMapper.selectById(video.getVideoId());
        CoverVO coverVO = new CoverVO();
        BeanUtils.copyProperties(videoCover, coverVO);

        videoDataVO.setCover(coverVO);

        videoDataVO.setCreateAt(video.getCreateTime());
        videoDataVO.setUpdateAt(video.getUpdateTime());

        return videoDataVO;

    }
    @Override
    public VideoDataVO getVideoDataById(String videoId) {

        Video video = videoMapper.selectById(videoId);

        return videoToVideoDataVO(video);
    }

    @Override
    public PageVO getVideoByName(PageRequest pageRequest,String videoName) {
        Page<Video> objectPage = new Page<>(pageRequest.getCurrentPage(), pageRequest.getPageSize());

        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();

        Page<Video> videoPage = videoMapper.selectPage(objectPage, videoQueryWrapper);


        List<Video> records = videoPage.getRecords();

        List<VideoDataVO> videoDataVOS = new ArrayList<>();
        //筛选records,并且迭代赋值
        for (Video video : records.stream()
                .filter(record -> record.getDescription().contains(videoName))
                .collect(Collectors.toList())) {
            videoDataVOS.add(videoToVideoDataVO(video));
        }

        return new PageVO(videoDataVOS, videoPage.getTotal()
                , videoPage.getSize()
                , videoPage.getCurrent());

        //TODO 先筛选再分页??
    }


    @Override
    public PageVO getVideoList(PageRequest pageRequest) {
        Page<Video> objectPage = new Page<>(pageRequest.getCurrentPage(), pageRequest.getPageSize());

        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();

        Page<Video> videoPage = videoMapper.selectPage(objectPage, videoQueryWrapper);

        List<Video> records = videoPage.getRecords();

        List<VideoDataVO> videoDataVOS = new ArrayList<>();

        for (Video video : records) {
            videoDataVOS.add(videoToVideoDataVO(video));
        }

        return new PageVO(videoDataVOS, videoPage.getTotal()
                , videoPage.getSize()
                , videoPage.getCurrent());
    }
}




