package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO {

    //评论id
    private String id;
    //视频id
    private String videoId;
    //评论内容
    private String content;
    //评论发起人id
    private String commentBy;
    //点赞数
    private Long likes;

    //头像
    private String avatar;

    private String createAt;

    private String updateAt;

}
