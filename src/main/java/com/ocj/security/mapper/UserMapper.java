package com.ocj.security.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocj.security.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
