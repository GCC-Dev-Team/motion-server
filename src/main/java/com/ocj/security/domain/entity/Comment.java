package com.ocj.security.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2023-10-28 18:36:31
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("comment")
public class Comment  {
    @TableId
    private String id;

    //视频id
    private String videoId;
    //评论内容
    private String content;
    //评论发起人id
    private String commentBy;
    //点赞数
    private Long likes;

    private String createAt;

    private String updateAt;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}

