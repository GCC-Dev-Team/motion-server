package com.ocj.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishVideoRequest {
    /**
     * 视频的描述
     */
    private String description;
    /**
     * 视频的分类id
     */
    private String categoryId;

    /**
     * 视频的标签
     */
    private String tags;

}
