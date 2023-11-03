package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.entity.Page;
import com.ocj.security.domain.dto.PublishVideoRequest;
import com.ocj.security.domain.entity.*;
import com.ocj.security.domain.vo.*;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.enums.OperationEnum;
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
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    QinuConfig qinuConfig;


    @Override
    public ResponseResult publishVideo(MultipartFile file, PublishVideoRequest publishVideoRequest) {

        String tags = publishVideoRequest.getTags();
        String description = publishVideoRequest.getDescription();
        if (!RegexCheckStringUtil.checkStringLength(description, 3, 80)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR, "描述字符超出80");
        }
        User user = SecurityUtils.getLoginUser().getUser();

        Video video = new Video();
        String videoId = RandomUtil.generateRandomString(16);
        video.setVideoId(videoId);
        video.setTags(tags);
        String domain = qinuConfig.getDomain();
        String fileUrl = fileService.uploadFile(file, "video/" + videoId);
        video.setUrl(fileUrl);
        video.setAddress(fileUrl.substring(domain.length() + 1));
        video.setStatus(1);
        video.setViews(0L);
        video.setLikeCount(0L);
        video.setDescription(description);
        video.setCategoryId(publishVideoRequest.getCategoryId());
        video.setPublisher(user.getId());

        Boolean processFile = fileService.processFile(video.getAddress(), OperationEnum.Video_Screenshot.getOperationOrder(), "videoCover/" + videoId + ".jpg");

        if (processFile.equals(Boolean.FALSE)) {
            return ResponseResult.errorResult(500, "视频截图处理失败!");
        }
        VideoCover videoCover = new VideoCover();
        videoCover.setVideoId(videoId);
        videoCover.setCoverAddress("videoCover/" + videoId + ".jpg");
        videoCover.setVideoCoverUrl(domain + "/" + videoCover.getCoverAddress());
        //这个是视频
        String videoCoverUrl = videoCover.getVideoCoverUrl();

        //TODO 优化，这个要睡觉，不行
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        CoverVO coverVO = fileService.urlGetPhotoImage(videoCoverUrl);

        videoCover.setWidth(coverVO.getWidth());
        videoCover.setHeight(coverVO.getHeight());
//
//
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
        videoQueryWrapper.eq("status", 1);
        System.out.println(categoryId);


        if (search != null) {
            videoQueryWrapper.like("description", search);
        }
        if (categoryId != null) {
            videoQueryWrapper.eq("category_id", categoryId);
        }
//       if (tag != null) {
//
//           videoQueryWrapper.like("", Arrays.stream(tag))
//
//        }
        return ResponseResult.okResult(getVideoList(page, videoQueryWrapper));
    }
}




