package com.ocj.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocj.security.domain.entity.Comment;
import org.apache.ibatis.annotations.Mapper;


/**
 * 评论表(Comment)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

}

