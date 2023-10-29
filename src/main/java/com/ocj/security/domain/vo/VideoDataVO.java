package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ocj.security.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDataVO {
    /**
     * 类型
     */
    private VideoCategoryVO videoCategoryVO;

    /**
     *用户信息
     */
    private VideoUserVO videoUserVO;
    /**
     * 视频的封面信息
     */
    private VideoCoverVO videoCoverVO;

    /**
     * 视频的评论条数(视频表没有的)
     */

    private int videoCommentCount;
    /**
     * 视频id
     */
    private String videoId;

    /**
     *视频url
     */
    private String url;

    /**
     *视频描述
     */
    private String description;

    /**
     *标签
     */
    private String tags;

    /**
     *喜欢数量
     */
    private Long likeCount;

    /**
     *观看数量
     */
    private Long views;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     *更新时间
     */

    private Date updateTime;

}
