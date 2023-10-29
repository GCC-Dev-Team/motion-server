package com.ocj.security.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoCoverVO {
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
    private Integer length;
}
