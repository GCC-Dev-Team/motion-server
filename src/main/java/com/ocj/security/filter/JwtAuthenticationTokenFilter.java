package com.ocj.security.filter;

import com.alibaba.fastjson.JSON;
import com.ocj.security.anno.NoNeedLogin;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.LoginUser;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.utils.JwtUtil;
import com.ocj.security.utils.RedisCache;
import com.ocj.security.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    //需要配置到SpringSecurity中
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //获取请求头中的token
        String token = request.getHeader("Authorization");


        if (!StringUtils.hasText(token)){
            //说明改接口不需要登录,直接放行
            filterChain.doFilter(request,response);
            return;
        }
        //表示需要登录的
//        if (StringUtils.hasText(token)){
//
//        }

        //解析token,获取userId
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            //token过期   token非法
            //响应告诉前端,需要重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //将JSON字符串写到响应体中
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        String userId = claims.getSubject();
        //从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + userId);

        //如果redis中获取不到用户信息
        if (Objects.isNull(loginUser)){
            //登录过期,请重新登录
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            //将JSON字符串写到响应体中
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }

        //将通过认证的用户信息存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request,response);
    }
}
