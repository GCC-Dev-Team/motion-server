package com.ocj.security.service;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    /**
     * 注册用户
     * @param registerRequest
     * @return
     */
    ResponseResult register(@RequestBody RegisterRequest registerRequest);
}
