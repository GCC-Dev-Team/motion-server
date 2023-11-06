package com.ocj.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDataVO implements Serializable {

    /**
     * 评论数量
     */
    private Integer commentCount;

    /**
     * 视频下的评论集合
     */
    private List<CommentVO> list;

}
