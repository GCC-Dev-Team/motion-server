package com.ocj.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.qiniu.common.QiniuException;

import java.util.List;


/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2023-10-28 18:36:31
 */
public interface CommentService extends IService<Comment> {

    AppHttpCodeEnum addComment(String videoId, String context) throws QiniuException;

    List<CommentVO> getCommentList(String videoId);

    int getCommentCount(String videoId);
    void addLikesCount(String commentId);
}

