package com.ocj.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoverVO {
    /**
     * 视频封面的地址
     */
    private String videoCoverUrl;

    /**
     *width是宽度
     */
    private Integer width;

    /**
     *封面的长度
     */
    private Integer height;
}
