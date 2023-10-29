package com.ocj.security.mapper;

import com.ocj.security.domain.entity.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author L
* @description 针对表【video】的数据库操作Mapper
* @createDate 2023-10-28 09:11:25
* @Entity com.ocj.security.domain.entity.Video
*/
public interface VideoMapper extends BaseMapper<Video> {

    @Select("SELECT video_id FROM video ORDER BY RAND() LIMIT 10")
    List<String> randVideoList();

}




