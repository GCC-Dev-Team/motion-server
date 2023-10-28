package com.ocj.security.domain.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest {

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

}
