package com.ocj.security.handler.security;

import com.alibaba.fastjson.JSON;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//需要配置到SpringSecurity
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    //权限不匹配捕获器
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        //打印异常信息
        e.printStackTrace();
        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        WebUtils.renderString(response, JSON.toJSONString(result));
    }
}
