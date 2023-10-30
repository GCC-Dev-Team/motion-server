package com.ocj.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.entity.LikeCommentVideo;
import com.ocj.security.domain.entity.User;
import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.exception.SystemException;
import com.ocj.security.mapper.CommentMapper;
import com.ocj.security.mapper.LikeCommentVideoMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.LikeCommentVideoService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.RedisCache;
import com.ocj.security.utils.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

import static com.ocj.security.enums.AppHttpCodeEnum.CONTEXT_NOT_NULL;
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
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;
    @Override
    public void addComment(String videoId,String content) {
        if (!StringUtils.hasText(content)){ //如果评论内容为空
            throw new SystemException(CONTEXT_NOT_NULL);
        }
        Comment comment = Comment.builder()
                .id(RandomUtil.generateRandomNumberString())
                .content(content)
                .videoId(videoId)
                .userId(SecurityUtils.getUserId())
                .likes(0L)
                .createAt(getCurrentTimeAsString())
                .updateAt(getCurrentTimeAsString())
                .build();

        save(comment);

        redisCache.deleteObject("commentVideo::"+videoId);

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
            User user = userMapper.selectById(commentVO.getUserId());
            commentVO.setUserName(user.getUserName());
            commentVO.setAvatar(user.getAvatar());
        }
        redisCache.setCacheList("commentVideo::"+videoId,commentVOList);


        return commentVOList;
    }

    /*@Override
    public List<CommentVO> getCommentList(String videoId) {
        //查询条件,获取该视频video下的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        queryWrapper.eq(Comment::getDelFlag,0);
        List<Comment> commentList = list(queryWrapper);
        List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);

        LambdaQueryWrapper<LikeCommentVideo> queryLike = new LambdaQueryWrapper<>();
        //根据userId查出该用户所有点赞的评论
        queryLike.eq(LikeCommentVideo::getUserId,SecurityUtils.getUserId());
        //queryLike.eq(LikeCommentVideo::getIsLiked,videoId);

        //查询该用户的所有点赞集合
        List<LikeCommentVideo> likeCommentVideos = likeCommentVideoMapper.selectList(queryLike);

        for (CommentVO commentVO :commentVOList){
            //对评论的 头像 和 用户名 赋值
            User user = userMapper.selectById(commentVO.getUserId());
            commentVO.setUserName(user.getUserName());
            commentVO.setAvatar(user.getAvatar());
            //在该视频的videoId评论下,在用户已点赞的评论下,设置对应用户的是否点赞情况
            if (likeCommentVideos.contains(commentVO.getId())){
                commentVO.setIsLiked(true);
            }else {
                commentVO.setIsLiked(false);
            }
        }

        redisCache.setCacheList("commentVideo::"+videoId,commentVOList);
        return commentVOList;
    }*/

    @Override
    public void addLikesCount(String videoId, String CommentId) {

    }

    @Override
    public void addLikesCount(String commentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId,commentId);
        Comment comment = getOne(queryWrapper);
        Long likes = comment.getLikes();
        comment.setLikes(  ++likes  );
        updateById(comment);
        /*String likes =  redisCache.getCacheObject(commentId+"likes:");
        Long likesLong;
        //如果该评论点赞前的点赞数likes==0
        if (likes==null){
            likes = "1";
        }else {
            //如果点赞前的likes!=0
            likesLong = Long.parseLong(likes);
            likes = (++likesLong).toString();
        }
        //查出要修改的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId,commentId);
        //修改后的评论的点赞数,保存到数据库中
        Comment comment = getOne(queryWrapper);
        comment.setLikes(Long.valueOf(likes));
        updateById(comment);
        String videoId = comment.getVideoId();
        //删除该视频的评论缓存
        redisCache.deleteObject("commentVideo::"+videoId);*/
    }

    public int getCommentCount(String videoId){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        return count(queryWrapper);
    }

}
