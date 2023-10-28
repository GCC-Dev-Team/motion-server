package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.exception.SystemException;
import com.ocj.security.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import com.ocj.security.service.CommentService;
import org.springframework.util.StringUtils;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
//@Service("commentService")
    @Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public void addComment(Comment comment) {
        //评论不能为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTEXT_NOT_NULL);
        }
        //TODO 设置MP字段填充
        save(comment);

    }


}

