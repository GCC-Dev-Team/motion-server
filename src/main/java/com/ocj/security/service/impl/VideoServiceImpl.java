package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.config.MinIoClientConfig;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.entity.Page;
import com.ocj.security.domain.dto.PublishVideoRequest;
import com.ocj.security.domain.entity.*;
import com.ocj.security.domain.vo.*;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.enums.OperationEnum;
import com.ocj.security.enums.RegexOrderEnum;
import com.ocj.security.mapper.CategoryMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.RegexCheckStringUtil;
import com.ocj.security.utils.SecurityUtils;
import com.ocj.security.utils.VideoCoverUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    @Resource
    VideoCoverUtils videoCoverUtils;

    @Resource
    MinIoClientConfig minIoClientConfig;


    @Transactional
    @Override
    public ResponseResult publishVideo(MultipartFile file, PublishVideoRequest publishVideoRequest) {

        String tags = publishVideoRequest.getTags();
        String description = publishVideoRequest.getDescription();
        if (!RegexCheckStringUtil.checkStringLength(description, 3, 80)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "描述字符超出80");
        }
        //User user = SecurityUtils.getLoginUser().getUser();

        Video video = new Video();
        String videoId = RandomUtil.generateRandomString(16);
        video.setVideoId(videoId);
        video.setTags(tags);

        String domain= minIoClientConfig.getUrl();
        String fileUrl = fileService.uploadFile(file,"raw",videoId);
        video.setUrl(fileUrl);
        video.setAddress(fileUrl.substring(domain.length() + 1));
        video.setStatus(1);
        video.setViews(0L);
        video.setLikeCount(0L);
        video.setDescription(description);
        video.setCategoryId(publishVideoRequest.getCategoryId());
        video.setPublisher("b2218631-d51f-4c28-a575-941fba921b0b");


        VideoCover videoCover = videoCoverUtils.fetchFrame(file, videoId);
        videoMapper.insert(video);
        videoCoverMapper.insert(videoCover);

        return ResponseResult.okResult(video.getUrl());
    }


    public VideoDataVO videoToVideoDataVO(Video video) {

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


    public PageVO getVideoList(Page page, QueryWrapper<Video> queryWrapper) {

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Video> videoPage = videoMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page.getCurrentPage(), page.getPageSize()),
                queryWrapper);

        List<Video> records = videoPage.getRecords();

        List<VideoDataVO> videoDataVOS = new ArrayList<>();

        for (Video video : records) {
            videoDataVOS.add(videoToVideoDataVO(video));
        }

        return new PageVO(videoDataVOS, videoPage.getTotal(),
                videoPage.getSize(), videoPage.getCurrent());
    }

    @Override
    public ResponseResult getVideoList(Integer currentPage, Integer pageSize, String search, String categoryId, String[] tag) {
        Page page = new Page(currentPage, pageSize);
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("status", 1).orderByDesc("create_time");


        if (search != null) {
            boolean check = RegexCheckStringUtil.regexCheck(search, RegexOrderEnum.Tag_Regex)&&
                    RegexCheckStringUtil.checkStringLength(search,0,8);

            if (check){
                videoQueryWrapper.like("description", search);
            }
        }

        if (categoryId != null) {

            int number = Integer.parseInt(categoryId);

            if (number >= 1 && number <= 8){
                videoQueryWrapper.eq("category_id", categoryId);
            }
        }

        if (tag != null) {
            //筛选了不包含#的标签
            StringBuilder tags=new StringBuilder();
            for (String str: tag){
                boolean check = RegexCheckStringUtil.regexCheck(str, RegexOrderEnum.Tag_Regex)&&
                        RegexCheckStringUtil.checkStringLength(str,1,6);
               //符合中英文和数字的
                if (check){
                    tags.append("#"+str);
                }
            }

            videoQueryWrapper.like("tags", tags.toString());
        }

        return ResponseResult.okResult(getVideoList(page, videoQueryWrapper));
    }

    @Override
    public ResponseResult getCategory() {


        List<Category> categories = categoryMapper.selectList(null);

        List<CategoryVO> categoryVOS=new ArrayList<>();
        for (Category category:categories){

            CategoryVO categoryVO = new CategoryVO();
            BeanUtils.copyProperties(category,categoryVO);

            categoryVOS.add(categoryVO);
        }

        return ResponseResult.okResult(categoryVOS);
    }


    @Override
    public ResponseResult previous(String videoId) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("status", 1).orderByDesc("create_time");

        List<Video> videos = videoMapper.selectList(videoQueryWrapper);
        int temple=0;
        for (Video video:videos){

            if (video.getVideoId().equals(videoId)){
                break;
            }
            temple=temple+1;
        }
        if (temple==0){
            return ResponseResult.okResult(videos.get(videos.size()-1).getVideoId());
        }
        return ResponseResult.okResult(videos.get(temple-1).getVideoId());
    }

    @Override
    public ResponseResult next(String videoId) {
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("status", 1).orderByDesc("create_time");

        List<Video> videos = videoMapper.selectList(videoQueryWrapper);
        int temple=0;
        for (Video video:videos){

            if (video.getVideoId().equals(videoId)){
                break;
            }
            temple=temple+1;
        }
        if (temple==videos.size()-1){
            return ResponseResult.okResult(videos.get(0).getVideoId());
        }
        return ResponseResult.okResult(videos.get(temple+1).getVideoId());
    }


}




