package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {

    @TableId
    private String id;
    //视频id
    private String videoId;
    //根评论id
    private String rootId;
    //评论内容
    private String content;
    //所回复的目标评论的用户id
    private String toCommentUserId;
    //回复目标评论id
    private String toCommentId;
    //评论发起人id
    private String createBy;

    private List<CommentVO> children;

    //创建时间
    private Date createTime;


}
