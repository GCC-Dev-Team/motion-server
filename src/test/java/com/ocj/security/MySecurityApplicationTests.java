package com.ocj.security;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.entity.Video;
import com.ocj.security.domain.entity.VideoCover;
import com.ocj.security.domain.vo.CoverVO;
import com.ocj.security.domain.vo.VideoDataVO;
import com.ocj.security.enums.OperationEnum;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.service.FileService;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.service.VideoService;
import com.ocj.security.utils.BeanCopyUtils;
import com.ocj.security.utils.FileToMultipartFileConverter;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.processing.OperationStatus;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import com.qiniu.util.StringUtils;
import com.qiniu.util.UrlSafeBase64;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@Slf4j

class MySecurityApplicationTests {

    @Resource
    VideoService videoService;


    @Resource
    QiniuApiService qiniuApiService;

    @Test

    public void uploadFile(MultipartFile file,String fileAddress){
        fileAddress = "avatar";
        String fileAddr = qiniuApiService.uploadFile(file,fileAddress);
    }


    @Test
    public  void test() throws QiniuException {
        String textCensor = qiniuApiService.TextCensor("asdafsffafafa");
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

        System.out.println("Suggestion: " + suggestion);
        System.out.println("label:" + label);
    }



//    @Test
//    void contextLoads() {
//
//        String folderPath = "D:\\抖音视频\\日常"; // 指定文件夹路径
//
//        File folder = new File(folderPath);
//
//        if (folder.isDirectory()) {
//            File[] files = folder.listFiles();
//
//            if (files != null) {
//                List<MultipartFile> multipartFiles = new ArrayList<>();
//
//                for (File file : files) {
//                    try {
//                        MultipartFile multipartFile = FileToMultipartFileConverter.convertFileToMultipartFile(file);
//                        multipartFiles.add(multipartFile);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // 现在您可以使用multipartFiles进行后续操作
//                for (int i = 0; i < multipartFiles.size(); i++) {
//                    videoService.addVideo(multipartFiles.get(i));
//                    System.out.printf(String.valueOf(i));
//                }
//            } else {
//                System.err.println("No files found in the specified folder.");
//            }
//        } else {
//            System.err.println("The specified path is not a folder.");
//        }
//
//    }
//
//    @Test
//    void cu(){
//        List<VideoDataVO> videoList = videoService.getVideoList();
//
//        System.out.println(videoList);
//    }

    @Resource
    QinuConfig qinuConfig;
    @Resource
    FileService fileService;

    @Resource
    VideoMapper videoMapper;
    @Resource
    VideoCoverMapper videoCoverMapper;

    @Test
    void updateData(){
//
//        List<Video> videos = videoMapper.selectList(null);
//
//        for (Video video:videos){
//
//            Video video1 = new Video();
//
//            BeanUtils.copyProperties(video,video1);
////            System.out.println();
//            video1.setVideoId("video"+video.getVideoId().substring(6));
//
//            VideoCover videoCover = new VideoCover();
//            VideoCover videoCover1 = videoCoverMapper.selectById(video.getVideoId());
//
//            BeanUtils.copyProperties(videoCover1,videoCover);
//            videoCover.setVideoId("video"+video.getVideoId().substring(6));
//
//            videoCoverMapper.insert(videoCover);
//            videoCoverMapper.deleteById(videoCover1);
//
//            videoMapper.insert(video1);
//            videoMapper.deleteById(video);
//
//            System.out.println("ok");
//        }
    }

    @Test
    void testPageLikeVideo(){
//        Page<Video> objectPage = new Page<>(1, 10);
//
//       String videoName="测试";
//        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
//        videoQueryWrapper.like("tags",videoName).or().like("description",videoName);
//
//        Page<Video> videoPage = videoMapper.selectPage(objectPage, videoQueryWrapper);
//
//
//        List<Video> records = videoPage.getRecords();
//
//        System.out.println(records.size());
//        long total = videoPage.getTotal();
//        System.out.println(total);
    }
    @Test
    void testFile(){
//        String url="http://s36fh9xu3.hn-bkt.clouddn.com/videoCover/cecf27b09a594777.jpg";
//        System.out.println(fileService.urlGetPhotoImage(url).toString());
//
//        Integer height = fileService.urlGetPhotoImage(url).getHeight();
//        Integer width = fileService.urlGetPhotoImage(url).getWidth();
//        VideoCover videoCover = new VideoCover();
//        videoCover.setWidth(width);
//        videoCover.setHeight(height);
//        String url="http://s36fh9xu3.hn-bkt.clouddn.com//videoCover/aa4ca052485b4934.jpg";
////
//        fileService.urlGetPhotoImage(url);
//        String domain= qinuConfig.getDomain();
//        System.out.println(url.substring(domain.length()+1));
//        Boolean b = fileService.processFile("video/aa4ca052485b4934.mp4", OperationEnum.Video_Screenshot.getOperationOrder(), "videoCover/aa4ca052485b4934.jpg");
//        System.out.println(b);
    }

    @Test
    public void enco() throws UnsupportedEncodingException {
//        // 参数一：要编码的字符串 参数二：指定字符集
//        String data="美女";
//        System.out.println(URLEncoder.encode(data, "utf-8"));

//        Page<Video> videoPage = videoMapper.selectPage(new Page<>(1,10),
//                null);
//        System.out.println(videoPage.getSize());
    }

}
