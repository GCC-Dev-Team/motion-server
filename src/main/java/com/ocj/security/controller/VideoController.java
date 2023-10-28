package com.ocj.security.controller;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.VideoService;
import com.ocj.security.utils.BeanCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 未测试，ing
 */
@RestController
@RequestMapping("/video")
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
    public ResponseResult<List<CommentVO>> getComment(@PathVariable String videoId){
        List<CommentVO> commentVOList =  commentService.getCommentList(videoId);
        return ResponseResult.okResult(commentVOList);
    }






}
