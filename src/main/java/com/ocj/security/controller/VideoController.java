package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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
    public ResponseResult addVideo(MultipartFile file){

        return videoService.addVideo(file);
    }

    @Resource
    CommentService commentService;
    @PostMapping("/{videoId}/comment")
    public ResponseResult addComment(@PathVariable String videoId, @RequestBody AddCommentRequest addCommentRequest){
        commentService.addComment(videoId,addCommentRequest.getContent());
        return ResponseResult.okResult();
    }

    @GetMapping("/{videoId}/comment")
    @Cacheable(cacheNames = "commentVideo",key = "#videoId")
    public ResponseResult<List<CommentVO>> getComment(@PathVariable String videoId){
        List<CommentVO> commentVOList =  commentService.getCommentList(videoId);
        return ResponseResult.okResult(commentVOList);
    }

    @PutMapping("/{videoId}/{CommentId}/like")
    public ResponseResult addLikesCountTest(@PathVariable String videoId,@PathVariable String CommentId){

        commentService.addLikesCount(videoId,CommentId);
        return ResponseResult.okResult();
    }






}
