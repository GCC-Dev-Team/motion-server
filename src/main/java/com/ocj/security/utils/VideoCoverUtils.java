package com.ocj.security.utils;

import com.ocj.security.domain.entity.VideoCover;
import com.ocj.security.domain.vo.CoverVO;
import com.ocj.security.service.FileService;
import lombok.extern.slf4j.Slf4j;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;

import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;
import org.springframework.beans.BeanUtils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 视频截取首帧工具
 */
@Slf4j
@Component
public class VideoCoverUtils {

    /*** 图片格式*/
    private static final String FILE_EXT = "jpg";

    /*** 帧数*/
    private static final int THUMB_FRAME = 1;

    @Resource
    FileService fileService;

    /**
     * 对截图首帧封装，返回一个videoCOver对象
     */
    public  VideoCover fetchFrame(MultipartFile videoFile, String videoId) {

        CoverVO coverVO = new CoverVO();
        try {
            File file = FileUtil.MultipartFileToFile(videoFile);

            MultipartFile multipartCoverFile = getThumbnail(file,coverVO);

            coverVO.setVideoCoverUrl(fileService.uploadFile(multipartCoverFile, "cover", videoId));

            FileUtil.FileDelete(file);

        } catch (JCodecException e) {
            return null;
        } catch (IOException e) {
            log.error(String.valueOf(new RuntimeException(e)));
            return null;
        }
        VideoCover videoCover = new VideoCover();
        videoCover.setVideoId(videoId);
        videoCover.setCoverAddress("cover/" + videoId + ".jpg");

        BeanUtils.copyProperties(coverVO,videoCover);

        return videoCover;
    }


    /**
     * 获取第一帧缩略图
     *
     * @param videoFile 视频路径
     */
    public static MultipartFile getThumbnail(File videoFile,CoverVO coverVO) throws JCodecException, IOException {
        // 根据扩展名创建一个新文件路径
        Picture picture = FrameGrab.getFrameFromFile(videoFile, THUMB_FRAME);
        BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
        coverVO.setWidth(bufferedImage.getWidth());
        coverVO.setHeight(bufferedImage.getHeight());
        return fileCase(bufferedImage);
    }

    /**
     * 截图后转成multipartFile文件
     *
     * @param image
     * @return
     * @throws IOException
     */

    public static MultipartFile fileCase(BufferedImage image) throws IOException {
        //将newImage写入字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        ImageIO.write(image, "."+FILE_EXT, baos);
        File fileTest = new File("test."+FILE_EXT);
        ImageIO.write(image, FILE_EXT, fileTest);
        //转换为MultipartFile
//        return new MockMultipartFile("xiaoliverygood.jpg", baos.toByteArray());
        return FileUtil.FileToMultipartFile(fileTest);
    }


}