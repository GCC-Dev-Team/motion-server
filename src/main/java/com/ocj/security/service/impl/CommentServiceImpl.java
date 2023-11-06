package com.ocj.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.ocj.security.domain.entity.Comment;
import com.ocj.security.domain.entity.LikeCommentVideo;
import com.ocj.security.domain.entity.User;

import com.ocj.security.domain.vo.CommentVO;
import com.ocj.security.enums.AppHttpCodeEnum;
import com.ocj.security.exception.SystemException;
import com.ocj.security.mapper.CommentMapper;
import com.ocj.security.mapper.LikeCommentVideoMapper;
import com.ocj.security.mapper.UserMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.LikeCommentVideoService;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.RandomUtil;
import com.ocj.security.utils.RedisCache;
import com.ocj.security.utils.SecurityUtils;
import com.qiniu.common.QiniuException;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Slf4j
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    private RedisCache redisCache;
    @Resource
    private UserMapper userMapper;
    @Resource
    QiniuApiService qiniuApiService;
    @Resource
    private LikeCommentVideoService likeCommentVideoService;
    /**
     * 增加评论
     */
    @Override
    public AppHttpCodeEnum addComment(String videoId, String content) throws QiniuException {
        if (!StringUtils.hasText(content)){ //如果评论内容为空
            throw new SystemException(CONTEXT_NOT_NULL);
        }

        //如果违规,返回true
        if (trailTxt(content)){
            return AppHttpCodeEnum.CONTENT_VIOLATION;
        }

        Comment comment = Comment.builder()
                .id(RandomUtil.generateRandomNumberString())
                .content(content)
                .videoId(videoId)
                .userId(SecurityUtils.getUserId())
                .likeCount(0L)
                .createAt(getCurrentTimeAsString())
                .updateAt(getCurrentTimeAsString())
                .build();

        save(comment);

        redisCache.deleteObject("commentDataVO::"+videoId);

        return AppHttpCodeEnum.SUCCESS;
    }


    @Resource
    private LikeCommentVideoMapper likeCommentVideoMapper;

    /**
     * 获取评论
     * @param videoId
     * @return
     */
    @Override
    public List<CommentVO> getCommentList(String videoId) {
        //查询条件,获取该视频video下的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        queryWrapper.eq(Comment::getDelFlag,0);
        List<Comment> commentList = list(queryWrapper);
        List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);

        try {
            //如果查看评论的用户已经登录
            SecurityUtils.getUserId();
        }catch (Exception ignored){
            log.info("用户未登录");
            //return ResponseResult.errorResult(NEED_LOGIN);
        }

        for (CommentVO commentVO : commentVOList){
            //对评论的 头像 和 用户名 赋值
            User user = userMapper.selectById(commentVO.getUserId());
            commentVO.setUserName(user.getUserName());
            commentVO.setAvatar(user.getAvatar());

            int isLiked=0;
            try {
                //如果查看评论的用户已经登录

                isLiked= likeCommentVideoMapper.isLiked(SecurityUtils.getUserId(), commentVO.getId());
            }catch (Exception ignored){
                log.info("用户未登录");
            }

            //如果对该评论已经点赞过
            try {
                if (isLiked > 0){
                    commentVO.setLiked(true);
                }
            } catch (Exception ignored) {
                log.info("未对评论:{}点赞",commentVO.getId());
                commentVO.setLiked(false);
            }
        }
        //根据更新时间时间排序
        commentVOList.sort((o1, o2) ->
            {
                if (o1.getUpdateAt().compareTo(o2.getUpdateAt()) < 0) {
                    return 1;
                }else if (o1.getUpdateAt().compareTo(o2.getUpdateAt()) >0 ){
                    return -1;
                }else {
                    return 0;
                }
            }
        );
        return commentVOList;
    }


    /**
     * 评论点赞/取消点赞
     * @param commentId
     */
    @Override
    public void addLikesCount(String commentId) {

        String userId = SecurityUtils.getUserId();
        //null 为false，表示没有点赞，点了的有记录值
        String clickLike = clickLike(commentId, userId);

        int temple=0;
        if (StringUtils.hasText(clickLike)){
            likeCommentVideoMapper.deleteById(clickLike);
            temple=-1;
        }else {
            //没有点赞的,现在增加记录
            //save Like
            LikeCommentVideo likeCommentVideo = new LikeCommentVideo();
            likeCommentVideo.setUserId(userId);
            likeCommentVideo.setIsLiked(commentId);
            likeCommentVideo.setCreateAt(getCurrentTimeAsString());
            likeCommentVideo.setUpdateAt(getCurrentTimeAsString());
            likeCommentVideo.setId(RandomUtil.generateRandomNumberString());
            likeCommentVideoService.saveLike(likeCommentVideo);
            temple=1;
        }

        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId,commentId);
        Comment comment = getOne(queryWrapper);
        Long likes = comment.getLikeCount();
        comment.setLikeCount(likes+temple);
        updateById(comment);

        //删除缓存
        redisCache.deleteObject("commentDataVO::"+comment.getVideoId());
    }

    /**
     * 七牛云文本内容审核
     * @param content
     * @return
     * @throws QiniuException
     */
    public boolean trailTxt(String content) throws QiniuException {
        String textCensor = qiniuApiService.TextCensor(content);
        log.info("七牛云返回:{}",textCensor);
        JSONObject jsonObject = JSON.parseObject(textCensor);


        //处理建议:pass-通过,block-建议删除
        String suggestion = jsonObject.getJSONObject("result")
                .getJSONObject("scenes")
                .getJSONObject("antispam")
                .getString("suggestion");


        String label = jsonObject.getJSONObject("result")
                .getJSONObject("scenes")
                .getJSONObject("antispam")
                .getJSONArray("details").getJSONObject(0).getString("label");

        log.info("处理建议: {}",suggestion);
        log.info("审核结果: {}",label);

        if (suggestion.equals("block")){
            return true;
        }
        return false;

    }

    /**
     * 获取评论数量
     * @param videoId
     * @return
     */
    public int getCommentCount(String videoId){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        return count(queryWrapper);

    }

    /**
     * 有点赞返回(记录id)，没有点赞返回null
     *
     * @param commentId
     * @param userId
     * @return
     */
    private String clickLike(String commentId, String userId){

        QueryWrapper<LikeCommentVideo> wrapper = new QueryWrapper<LikeCommentVideo>().eq("user_id", userId).eq("is_liked", commentId);
        LikeCommentVideo likeCommentVideo = likeCommentVideoMapper.selectOne(wrapper);

        //没有点赞的
        if (likeCommentVideo ==null){
            return null;
        }

        return likeCommentVideo.getId();
    }

}
