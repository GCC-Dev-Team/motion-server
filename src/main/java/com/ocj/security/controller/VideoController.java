package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.dto.PublishVideoRequest;

import com.ocj.security.domain.vo.CommentDataVO;
import com.ocj.security.domain.vo.CommentVO;
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
 * video
 */
@RestController
@RequestMapping("/video")
@Slf4j
public class VideoController {
    @Resource
    VideoService videoService;
    @PostMapping("/publish")
    public ResponseResult publishVideo(MultipartFile file,String description,String categoryId,String tags ){
        PublishVideoRequest publishVideoRequest = new PublishVideoRequest(description, categoryId, tags);

        return videoService.publishVideo(file,publishVideoRequest);
    }



    @GetMapping(value = "/list",produces = "application/json; charset=utf-8")
    public ResponseResult getVideoList(@RequestParam("currentPage") Integer currentPage, @RequestParam("pageSize") Integer pageSize, String search, String categoryId, String [] tag){

        return videoService.getVideoList(currentPage,pageSize,search,categoryId,tag);
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
    @Cacheable(value = "commentDataVO",key = "#videoId")
    public ResponseResult<CommentDataVO> getComment(@PathVariable String videoId){
        List<CommentVO> commentVOList = new ArrayList<>();
        try {
            //尝试获取评论
            commentVOList = commentService.getCommentList(videoId);

            CommentDataVO commentDataVO = new CommentDataVO(commentService.getCommentCount(videoId), commentVOList);
            return ResponseResult.okResult(commentDataVO);
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

    @GetMapping("/category")
    public ResponseResult getCategory(){
        return videoService.getCategory();
    }


    /**
     * 当前的视频id查看上一个视频id
     * @param videoId
     * @return
     */
    @GetMapping(value = "/previous/{videoId}",produces = "application/json; charset=utf-8")
    public ResponseResult previous(@PathVariable String videoId){

        return videoService.previous(videoId);
    }


    /**
     * 当前的视频id查看下一个视频id
     * @param videoId
     * @return
     */

    @GetMapping(value = "/next/{videoId}",produces = "application/json; charset=utf-8")
    public ResponseResult next(@PathVariable String videoId){

        return videoService.next(videoId);
    }





}
