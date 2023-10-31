package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.domain.entity.Category;
import com.ocj.security.service.CategoryService;
import com.ocj.security.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

import java.awt.*;

/**
* @author L
* @description 针对表【category】的数据库操作Service实现
* @createDate 2023-10-28 09:11:41
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

}




