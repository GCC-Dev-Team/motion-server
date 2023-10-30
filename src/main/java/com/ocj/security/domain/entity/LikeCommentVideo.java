package com.ocj.security.domain.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (LikeCommentVideo)表实体类
 *
 * @author makejava
 * @since 2023-10-30 16:49:17
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("like_comment_video")
public class LikeCommentVideo implements Serializable {
    @TableId
    private Integer id;


    private String userId;
    //喜欢的:视频id/评论id
    private String isLiked;



}

