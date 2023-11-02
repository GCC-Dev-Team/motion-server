package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.dto.AddVideoRequest;
import com.ocj.security.domain.dto.PageRequest;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.domain.vo.VideoDataVO;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.VideoService;
import com.qiniu.common.QiniuException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 未测试，ing
 */
@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {
    @Resource
    VideoService videoService;
    @PostMapping("/addVideo")
    public ResponseResult addVideo(MultipartFile file, @RequestBody AddVideoRequest addVideoRequest){

        return null;
    }

    /**
     * 获取视频列表
     * @return
     */
    @GetMapping("/list")
    public ResponseResult getVideoList(Integer currentPage,Integer pageSize){
        PageRequest pageRequest=new PageRequest(currentPage,pageSize);
        return ResponseResult.okResult(videoService.getVideoList(pageRequest));
    }

    /**
     * 获取每个视频详细信息
     * @param videoId
     * @return
     */
    @GetMapping("/one")
    public ResponseResult getVideoById(String videoId){

        return ResponseResult.okResult(videoService.getVideoDataById(videoId));
    }

    @Resource
    CommentService commentService;
    @PostMapping("/{videoId}/comment")
    public ResponseResult<AppHttpCodeEnum> addComment(@PathVariable String videoId, @RequestBody AddCommentRequest addCommentRequest) throws QiniuException {
        AppHttpCodeEnum appHttpCodeEnumEnum = commentService.addComment(videoId, addCommentRequest.getContent());
        return ResponseResult.errorResult(appHttpCodeEnumEnum);
    }

    @GetMapping("/{videoId}/comment/list")
    @Cacheable(value = "commentVideo",key = "#videoId")
    public ResponseResult<List<CommentVO>> getComment(@PathVariable String videoId){
        List<CommentVO> commentVOList = new ArrayList<>();
        try {
            //尝试获取评论
            commentVOList = commentService.getCommentList(videoId);
            return ResponseResult.okResult(commentVOList);
        } catch (Exception e) {
            //获取失败:无评论
            return ResponseResult.okResult(commentVOList);
        }
    }

    @PostMapping ("/comment/{commentId}/like")
    public ResponseResult addLikesCount(@PathVariable String commentId){
        commentService.addLikesCount(commentId);
        return ResponseResult.okResult();
    }


    /**
     * @param videoName
     *
    */
    @GetMapping(value = "/list/search/{videoName}",produces = "text/html;charset=UTF-8")
    public ResponseResult<List<VideoDataVO>> getVideoByName(@PathVariable String videoName,Integer currentPage,Integer pageSize){

        PageRequest pageRequest=new PageRequest(currentPage,pageSize);
        videoService.getVideoByName(pageRequest,videoName);

        return ResponseResult.okResult();
    }

    @GetMapping("/abc")
    public String testABC(){

        return "hello kizuna";
    }




}
