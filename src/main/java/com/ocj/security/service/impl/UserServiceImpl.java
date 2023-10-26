package com.ocj.security.service.impl;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.domain.entity.User;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.UserService;
import com.ocj.security.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public ResponseResult register(RegisterRequest registerRequest) {
        //情况一：用户名有重复

        //情况二：邮箱重复

        //情况三，密码格式有限制

        //添加到数据库
        User user = new User();
        user.setId(UUID.randomUUID().getMostSignificantBits());

        BeanUtils.copyProperties(registerRequest,user);

        userMapper.insert(user);

        //返回注册成功的消息
        return ResponseResult.okResult("注册成功");
    }
}
