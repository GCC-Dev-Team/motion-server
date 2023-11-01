package com.ocj.security.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.ocj.security.enums.AppHttpCodeEnum.CONTENT_VIOLATION;
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
    private LikeCommentVideoService likeCommentVideoService;
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

        redisCache.deleteObject("commentVideo::"+videoId);

        return AppHttpCodeEnum.SUCCESS;
    }



    @Override
    public List<CommentVO> getCommentList(String videoId) {
        //查询条件,获取该视频video下的评论
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        queryWrapper.eq(Comment::getDelFlag,0);
        List<Comment> commentList = list(queryWrapper);
        List<CommentVO> commentVOList = BeanCopyUtils.copyBeanList(commentList, CommentVO.class);

        LambdaQueryWrapper<LikeCommentVideo> queryLike = new LambdaQueryWrapper<>();
        try {
            //如果查看评论的用户已经登录
            queryLike.eq(LikeCommentVideo::getUserId,SecurityUtils.getUserId());
        }catch (Exception ignored){
            log.info("用户未登录");
        }
        //对评论的 头像 和 用户名 赋值
        for (CommentVO commentVO :commentVOList){
            User user = userMapper.selectById(commentVO.getUserId());
            commentVO.setUserName(user.getUserName());
            commentVO.setAvatar(user.getAvatar());
            //在点赞表中,用户对应的数据
            queryLike.eq(LikeCommentVideo::getIsLiked,commentVO.getId());
            //数量>1:点赞过
            likeCommentVideoService.count(queryLike);
            //如果已经登录,并且对该评论已经点赞过
            try {
                if (SecurityUtils.getUserId()!=null &&(likeCommentVideoService.count(queryLike))!=0){
                    commentVO.setLiked(true);
                }
            } catch (Exception ignored) {

            }

        }
        redisCache.setCacheList("commentVideo::"+videoId,commentVOList);
        return commentVOList;
    }

    @Override
    public void addLikesCount(String videoId, String CommentId) {

    }

    @Resource
    private LikeCommentVideoMapper likeCommentVideoMapper;

    @Override
    public void addLikesCount(String commentId) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getId,commentId);
        Comment comment = getOne(queryWrapper);
        Long likes = comment.getLikeCount();
        comment.setLikeCount(  ++likes  );
        updateById(comment);

        LikeCommentVideo likeCommentVideo = new LikeCommentVideo();

        likeCommentVideo.setUserId(SecurityUtils.getUserId());
        likeCommentVideo.setIsLiked(commentId);
        likeCommentVideo.setCreateAt(getCurrentTimeAsString());
        likeCommentVideo.setUpdateAt(getCurrentTimeAsString());
        likeCommentVideo.setId(RandomUtil.generateRandomNumberString());
        //likeCommentVideoMapper.insert(likeCommentVideo);
        //likeCommentVideoService.save(likeCommentVideo);
        likeCommentVideoService.saveLike(likeCommentVideo);

    }

    @Resource
    QiniuApiService qiniuApiService;
    //七牛云文本内容审核
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

    public int getCommentCount(String videoId){
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getVideoId,videoId);
        return count(queryWrapper);

    }

}
