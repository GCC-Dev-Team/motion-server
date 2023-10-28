package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.domain.entity.Video;
import com.ocj.security.service.VideoService;
import com.ocj.security.mapper.VideoMapper;
import org.springframework.stereotype.Service;

/**
* @author L
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-10-28 09:11:25
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




