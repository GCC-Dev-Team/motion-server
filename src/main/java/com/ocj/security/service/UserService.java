package com.ocj.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.entity.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService {
    /**
     * 注册用户
     * @param registerRequest
     * @return
     */
    ResponseResult register(@RequestBody RegisterRequest registerRequest);

    void setAvatar(User user);

    ResponseResult userInfo();


}
