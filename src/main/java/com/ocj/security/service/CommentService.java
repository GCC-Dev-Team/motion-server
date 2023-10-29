package com.ocj.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;

import java.util.List;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-10-28 18:36:31
 */
public interface CommentService extends IService<Comment> {

    void addComment(String videoId,String context);

    List<CommentVO> getCommentList(String videoId);

    void addLikesCount(String videoId, String CommentId);
}

