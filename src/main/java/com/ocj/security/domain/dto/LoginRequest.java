package com.ocj.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    /**
     * 用户名
     */

    private String userName;

    /**
     * 密码
     */
    private String password;
}
