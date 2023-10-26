package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.domain.entity.User;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.UserService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.CheckStringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<User>();
        userQueryWrapper.eq("user_name",registerRequest.getUserName());
        User sqlUser = userMapper.selectOne(userQueryWrapper);
        if (sqlUser!=null){
            return ResponseResult.errorResult(2001,"用户名已存在!");
        }

        //情况二：邮箱重复

        QueryWrapper<User> userQueryWrapperTwo = new QueryWrapper<User>();
        userQueryWrapperTwo.eq("email",registerRequest.getEmail());
        User sqlUserTwo = userMapper.selectOne(userQueryWrapperTwo);
        if (sqlUserTwo!=null){
            return ResponseResult.errorResult(2002,"该邮箱已注册!");
        }

        //情况三，密码格式有限制
        boolean secureString = CheckStringUtil.isSecureString(registerRequest.getPassword());
        if (!secureString){
            return ResponseResult.errorResult(2003,"密码安全性低!");
        }


        //添加到数据库
        User user = new User();
        user.setId(UUID.randomUUID().getMostSignificantBits());

        BeanUtils.copyProperties(registerRequest,user);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));

        userMapper.insert(user);

        //返回注册成功的消息
        return ResponseResult.okResult("注册成功");
    }
}
