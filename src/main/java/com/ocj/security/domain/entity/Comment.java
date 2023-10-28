package com.ocj.security.domain.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 评论表(Comment)表实体类
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment  {
    @TableId
    private Long id;

    //视频id
    private Long videoId;
    //根评论id
    private Long rootId;
    //评论内容
    private String content;
    //所回复的目标评论的用户id
    private String toCommentUserId;
    //回复目标评论id
    private Long toCommentId;
    //评论发起人id
    private String createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}

