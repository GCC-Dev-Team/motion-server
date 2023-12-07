package com.ocj.security.controller;


import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.entity.LoginUser;
import com.ocj.security.domain.entity.User;
import com.ocj.security.domain.vo.AvatarVO;
import com.ocj.security.service.FileService;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.service.UserService;
import com.ocj.security.utils.RedisCache;
import com.ocj.security.utils.SecurityUtils;
import com.qiniu.rtc.model.RoomResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private QiniuApiService qiniuApiService;

    @Resource
    private UserService userService;

    @Resource
    private RedisCache redisCache;
    @Resource
    FileService fileService;

    /**
     *上传头像
     * @param file
     * @return
     */
    @PostMapping("/avatar")
    @Transactional
    public ResponseResult uploadFile(MultipartFile file){
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        log.info("文件名:{}",originalFilename);
        String avatarURL = fileService.uploadFile(file,"avatar",originalFilename);

        //获取用户个人信息,修改头像
        User user = SecurityUtils.getLoginUser().getUser();
        user.setAvatar(avatarURL);
        userService.setAvatar(user);
        log.info(avatarURL);

        //更新个人信息缓存
        ReloadUserInfoRedis(user);
        return ResponseResult.okResult(new AvatarVO(avatarURL));
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    @Transactional
    public ResponseResult userInfo(){
        return userService.userInfo();
    }

    /**
     * 更新用户信息后,刷新用户信息缓存
     *
    **/

    private void ReloadUserInfoRedis(User user){
        //存入更新后的信息
        LoginUser loginUser = redisCache.getCacheObject("login:" + user.getId());
        loginUser.setUser(user);
        redisCache.setCacheObject("login:"+ user.getId(),loginUser);
    }

}
