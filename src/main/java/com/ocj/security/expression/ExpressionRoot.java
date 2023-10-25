package com.ocj.security.expression;


import com.ocj.security.domain.entity.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ex")
public class ExpressionRoot {
    /*public boolean hasAuthority(String authority){
        //获取当前用户的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        //判断用户权限集合中是否存在authority,如果存在返回true,否则返回false
        List<String> permissions = loginUser.getPermissions();
        return permissions.contains(authority);
    }*/
}
