package com.ocj.security.controller;



import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.service.CommentService;
import com.ocj.security.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论表(Comment)表控制层
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
@RestController
@RequestMapping("/comment")
public class CommentController{

    @Autowired
    private CommentService commentService;

    @PostMapping("/addComment")
    public ResponseResult addComment(@RequestBody AddCommentRequest addCommentRequest){
        Comment comment = BeanCopyUtils.copyBean(addCommentRequest, Comment.class);
        commentService.addComment(comment);
        return ResponseResult.okResult();
    }

    @GetMapping("/getCommentList")
    public ResponseResult<List<CommentVO>> getComment(){

        List<CommentVO> commentVOList = new ArrayList<>();

        commentVOList = commentService.getCommentList();

        return ResponseResult.okResult(commentVOList);

    }


}

