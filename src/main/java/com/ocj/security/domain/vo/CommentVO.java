package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentVO implements Serializable {

    //评论id
    private String id;
    //视频id
    /*private String videoId;*/
    //评论内容
    private String content;
    //评论发起人id
    private String userId;
    //评论者用户名
    private String userName;

    //点赞数
    private Long likes;

    //是否被该用户点赞(true:是,false:否)
    private Boolean isLiked;

    //头像
    private String avatar;

    private String createAt;

    private String updateAt;

}
