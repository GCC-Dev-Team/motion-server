package com.ocj.security.service.impl;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.LoginRequest;
import com.ocj.security.domain.entity.LoginUser;
import com.ocj.security.domain.entity.User;
import com.ocj.security.domain.vo.UserInfoVO;
import com.ocj.security.domain.vo.UserLoginVO;
import com.ocj.security.service.LoginService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.JwtUtil;
import com.ocj.security.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(LoginRequest user) {    //这个user为前端待验证的user

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());

        /*
            这里最终会调用UserDetailsService实现类中的方法:loadUserByUsername(String username),
          返回LoginUser对象赋值给authenticate
            如果认证失败或者查询不到对应的用户,authenticate则为null
         */
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);


        //是否被
        /*if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }*/

        //获取userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId();
        String jwt = JwtUtil.createJWT(userId);
        //把用户信息存入redis
        redisCache.setCacheObject("login:" + userId,loginUser);

        //把token和userinfo封装 返回
        //把User转换成UserInfoVo
        UserInfoVO userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVO.class);
        UserLoginVO userLoginVo = new UserLoginVO(jwt,userInfoVo);

        return ResponseResult.okResult(userLoginVo);
    }


    @Override
    public ResponseResult logout() {
        //获取token,解析获取userId
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userId
        String userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }


}
