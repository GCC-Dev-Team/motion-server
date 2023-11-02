package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.RegisterRequest;
import com.ocj.security.domain.entity.User;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.UserService;
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
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_name",registerRequest.getUserName());
        User sqlUser = userMapper.selectOne(userQueryWrapper);
        if(!CheckStringUtil.isUserName(registerRequest.getUserName())){
            return ResponseResult.errorResult(2005,"用户名必须是英文和数字组合，不可出现其他字符");
        }
        if (sqlUser!=null){
            return ResponseResult.errorResult(2001,"用户名已存在!");
        }

        //情况二：邮箱重复或者邮箱名称不符合正则表达式
        boolean emailCheck = CheckStringUtil.isEmail(registerRequest.getEmail());
        QueryWrapper<User> userQueryWrapperTwo = new QueryWrapper<>();
        userQueryWrapperTwo.eq("email",registerRequest.getEmail());
        User sqlUserTwo = userMapper.selectOne(userQueryWrapperTwo);
        if (sqlUserTwo!=null){
            return ResponseResult.errorResult(2002,"该邮箱已注册!");
        }
        if (!emailCheck){
            return ResponseResult.errorResult(2004,"请检查该邮箱是否正确!");
        }

        //情况三，密码格式有限制
        boolean secureString = CheckStringUtil.isSecureString(registerRequest.getPassword());
        if (!secureString){
            return ResponseResult.errorResult(2003,"密码安全性低!密码包含至少一个字母、至少一个数字，并且总长度大于8位");
        }


        //添加到数据库
        User user = new User();
        user.setId(UUID.randomUUID().toString());

        BeanUtils.copyProperties(registerRequest,user);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));

        userMapper.insert(user);

        //返回注册成功的消息
        return ResponseResult.okResult("注册成功");
    }

    @Override
    public void setAvatar(String userId, String avatarURL) {
        User user = userMapper.selectById(userId);
        user.setAvatar(avatarURL);
        userMapper.updateById(user);
    }
}
