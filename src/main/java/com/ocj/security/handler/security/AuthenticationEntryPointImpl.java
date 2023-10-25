package com.ocj.security.handler.security;

import com.alibaba.fastjson.JSON;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.utils.WebUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//需要配置到SpringSecurity
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    //认证失败捕获器
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //InsufficientAuthenticationException:使用了需要登录的功能,但是未登录
        //BadCredentialsException:账号或密码错误
        //打印异常信息
        e.printStackTrace();

        ResponseResult result = null;

        if (e instanceof BadCredentialsException){//账号或密码错误
            result = ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_ERROR.getCode(),AppHttpCodeEnum.LOGIN_ERROR.getMsg());
        }else if (e instanceof InsufficientAuthenticationException){//使用了需要登录的功能,但是未登录
            result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN.getCode(), AppHttpCodeEnum.NEED_LOGIN.getMsg());
        }else {
            result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(), "认证或授权失败");
        }

        //响应给前端
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
