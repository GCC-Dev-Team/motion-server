package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.domain.entity.LikeCommentVideo;
import com.ocj.security.mapper.LikeCommentVideoMapper;
import com.ocj.security.service.LikeCommentVideoService;
import org.springframework.stereotype.Service;

/**
 * (LikeCommentVideo)表服务实现类
 *
 * @author makejava
 * @since 2023-10-31 18:47:44
 */
@Service("likeCommentVideoService")
public class LikeCommentVideoServiceImpl extends ServiceImpl<LikeCommentVideoMapper, LikeCommentVideo> implements LikeCommentVideoService {

    public void saveLike(LikeCommentVideo likeCommentVideo){
        save(likeCommentVideo);
    }


}

