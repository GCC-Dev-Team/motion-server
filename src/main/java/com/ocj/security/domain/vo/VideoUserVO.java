package com.ocj.security.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoUserVO {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;
}
