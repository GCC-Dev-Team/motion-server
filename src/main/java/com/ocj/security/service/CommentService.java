package com.ocj.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
public interface CommentService extends IService<Comment> {

    void addComment(Comment comment);
}

