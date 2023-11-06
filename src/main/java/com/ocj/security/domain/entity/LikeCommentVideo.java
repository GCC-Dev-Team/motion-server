package com.ocj.security.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (LikeCommentVideo)表实体类
 *
 * @author makejava
 * @since 2023-10-31 18:51:13
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("like_comment_video")
public class LikeCommentVideo  {
    @TableId
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 喜欢的:视频id/评论id
     */
    private String isLiked;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 更新时间
     */
    private String updateAt;



}

