package com.ocj.security.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    /**
     * 用户名
     */
    private String userName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码
     */
    private String password;
}
