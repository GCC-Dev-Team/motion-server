package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.dto.AddCommentRequest;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.exception.SystemException;
import com.ocj.security.mapper.CommentMapper;
import com.ocj.security.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import com.ocj.security.service.CommentService;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.ocj.security.utils.RandomUtil.generateRandomNumberString;
import static com.ocj.security.utils.RandomUtil.generateRandomString;
import static com.ocj.security.utils.SecurityUtils.getLoginUser;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-10-28 01:43:21
 */
//@Service("commentService")
    @Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Override
    public void addComment(Comment comment) {
        //评论不能为空
        if (!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.CONTEXT_NOT_NULL);
        }

        comment.setCreateBy(getLoginUser().getUser().getId());
        comment.setId(generateRandomNumberString());
        //TODO 设置MP字段填充
        save(comment);

    }

    @Override
    public List<CommentVO> getCommentList() {

        //查找根评论rootId==-1
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,"-1");
        List<Comment> commentList = list(queryWrapper);

        //根评论集合
        List<CommentVO> commentRootList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);

        //对每一个根评论查找子评论
        commentRootList.stream()
                //传入根评论自己的id
                .forEach(CommentVO -> {
                    CommentVO.setChildren(getChildren(CommentVO.getId()));
                      //TODO 设置评论时间
                }
                );


        return commentRootList;
    }

    public List<CommentVO> getChildren(String id){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        //根据根评论的id,查找对应子评论的
        queryWrapper.eq(Comment::getRootId,id);
        List<Comment> commentChildrenList = list(queryWrapper);
        //转为VO
        List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentChildrenList, CommentVO.class);
        //返回该根评论对应的子评论集合
        return commentVOList;
    }


}

