package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.entity.User;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.mapper.CommentMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.ocj.security.utils.CurrentTimeUtil.getCurrentTimeAsString;


/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2023-10-28 18:36:31
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private UserMapper userMapper;
    @Override
    public void addComment(String videoId,String content) {

        Comment comment = Comment.builder()
                .id(RandomUtil.generateRandomNumberString())
                .content(content)
                .videoId(videoId)
                .commentBy(SecurityUtils.getUserId())
                .likes(0L)
                .createAt(getCurrentTimeAsString())
                .updateAt(getCurrentTimeAsString())
                .build();

        save(comment);

    }

    @Override
    public List<CommentVO> getCommentList(String videoId) {
        //查询条件,获取该视频video下的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        queryWrapper.eq(Comment::getDelFlag,0);
        List<Comment> commentList = list(queryWrapper);

        List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);

        //对评论的 头像 和 用户名 赋值
        for (CommentVO commentVO :commentVOList){
            User user = userMapper.selectById(commentVO.getCommentBy());
            commentVO.setUserName(user.getUserName());
            commentVO.setAvatar(user.getAvatar());
        }

        return commentVOList;

    }


}

