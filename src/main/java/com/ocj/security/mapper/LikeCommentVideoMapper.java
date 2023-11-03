package com.ocj.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocj.security.domain.entity.LikeCommentVideo;
import org.apache.ibatis.annotations.Select;


/**
 * (LikeCommentVideo)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-31 18:47:44
 */
public interface LikeCommentVideoMapper extends BaseMapper<LikeCommentVideo> {
    @Select("SELECT count(*) FROM like_comment_video where user_id = #{userId} and is_liked = #{isLiked}")
    int isLiked(String userId,String isLiked);
}

