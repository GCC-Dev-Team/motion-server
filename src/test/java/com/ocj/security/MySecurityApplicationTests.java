package com.ocj.security;

import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.dto.PublishVideoRequest;
import com.ocj.security.domain.vo.CoverVO;
import com.ocj.security.mapper.LikeCommentVideoMapper;
import com.ocj.security.mapper.VideoCoverMapper;
import com.ocj.security.mapper.VideoMapper;
import com.ocj.security.service.CommentService;
import com.ocj.security.service.FileService;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.service.VideoService;
import com.ocj.security.utils.FileToMultipartFileConverter;
import com.ocj.security.utils.FileUtil;
import com.ocj.security.utils.VideoCoverUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.jcodec.api.JCodecException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
@Slf4j
class MySecurityApplicationTests {

    @Resource
    VideoService videoService;


    @Resource
    QiniuApiService qiniuApiService;



    @Test
    void testCoverVideo() throws IOException {

//        String folderPath = "D:\\test"; // 指定文件夹路径
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
//                    File file = FileUtil.MultipartFileToFile(multipartFiles.get(i));
//
//
//                    FileUtil.FileDelete(file);
//                    //  MultipartFile cover = VideoCoverUtils.fetchFrame(multipartFiles.get(i));
////                    MultipartFile multipartFile = FileToMultipartFileConverter.convertFileToMultipartFile(cover);
//
////                    fileService.uploadFile(multipartFile,"cover","test");
//                }
//            } else {
//                System.err.println("No files found in the specified folder.");
//            }
//        } else {
//            System.err.println("The specified path is not a folder.");
//        }
    }

    @Test
    void uploadFile(MultipartFile file, String fileAddress) {
//        fileAddress = "avatar";
//        String fileAddr = qiniuApiService.uploadFile(file, fileAddress);
    }


    @Test
    void test() throws QiniuException {
//        String textCensor = qiniuApiService.TextCensor("asdafsffafafa");
//        log.info("七牛云返回:{}",textCensor);
//        JSONObject jsonObject = JSON.parseObject(textCensor);
//
//        //处理建议:pass-通过,block-建议删除
//        String suggestion = jsonObject.getJSONObject("result")
//                .getJSONObject("scenes")
//                .getJSONObject("antispam")
//                .getString("suggestion");
//
//
//        String label = jsonObject.getJSONObject("result")
//                .getJSONObject("scenes")
//                .getJSONObject("antispam")
//                .getJSONArray("details").getJSONObject(0).getString("label");
//
//        System.out.println("Suggestion: " + suggestion);
//        System.out.println("label:" + label);
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
    void updateData() {
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
    void testPageLikeVideo() {
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
    void testFile() {
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

    @Resource
    CommentService commentService;
    @Resource
    LikeCommentVideoMapper likeCommentVideoMapper;

    @Test
    void enco() throws UnsupportedEncodingException {
//        // 参数一：要编码的字符串 参数二：指定字符集
//        String data="美女";
//        System.out.println(URLEncoder.encode(data, "utf-8"));

//        Page<Video> videoPage = videoMapper.selectPage(new Page<>(1,10),
//                null);
////        System.out.println(videoPage.getSize());
//        ArrayList<String> tag = new ArrayList<>();
//        tag.add("ui");
//        tag.add("#kwo");
//        tag.add("+ko");
//        tag.add("uahi");
//        System.out.println(tag.size());
//        List<String> collect = tag.stream().filter(p -> !p.contains("#")).collect(Collectors.toList());
//        System.out.println(collect);

//        String str="汽车";
//        boolean check = RegexCheckStringUtil.regexCheck(str, RegexOrderEnum.Tag_Regex)&&
//                RegexCheckStringUtil.checkStringLength(str,1,6);
//        System.out.println(check);
        //System.out.println(commentService.getCommentCount("video01f9f89c030a41b9"));

        // System.out.println(videoService.getVideoDataById("video01f9f89c030a41b9").toString());
//        QueryWrapper<LikeCommentVideo> wrapper = new QueryWrapper<LikeCommentVideo>().eq("user_id", "3413c310-f4ae-43fd65").eq("is_liked", "5774798523901742");
//        System.out.println(likeCommentVideoMapper.selectOne(wrapper));
//        String clickLike="null";
//        System.out.println(StringUtils.hasText(clickLike));

        // System.out.println(likeCommentVideoMapper.isLiked("b2218631-d51f-4c28-a575-941fba921b0b", "6222401340071668"));
    }

    @Test
    void testFi() throws IOException, JCodecException {
        // 指定文件夹路径  "D://抖音视频/私密";
        String folderPath = "D://test";

        // 创建 File 对象
        File folder = new File(folderPath);

        // 检查文件夹是否存在
        if (folder.exists() && folder.isDirectory()) {
            // 获取文件夹中的所有文件
            File[] files = folder.listFiles();

            // 遍历文件列表并获取文件名
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    // 去掉文件名末尾的 .mp4
                    if (fileName.endsWith(".mp4")) {
                        fileName = fileName.substring(0, fileName.length() - 4);
                    }
                    //System.out.println("文件名: " + fileName);

                    MultipartFile multipartFile = FileToMultipartFileConverter.convertFileToMultipartFile(file);

                    File fili = FileUtil.MultipartFileToFile(multipartFile);

                    MultipartFile multipartCoverFile = VideoCoverUtils.getThumbnail(file,new CoverVO());

                    System.out.println(multipartCoverFile.getName());
                    videoService.publishVideo(multipartFile,new PublishVideoRequest(fileName,"2","#喜欢"));
                }
            }
        } else {
            System.err.println("指定的路径不是一个有效的文件夹。");
        }


    }

    @Test
    void Update() {
//
//        File file = FileUtil.MultipartFileToFile(videoFile);
//
//        MultipartFile multipartCoverFile = getThumbnail(file,coverVO);
    }

    @Resource
    Auth auth;
    @Test
    void abed() throws QiniuException {
        SmsManager smsManager = new SmsManager(auth);

        Response response = smsManager.sendMessage("1316", new String[]{"19835930193"}, new HashMap<>(12, 2));
        System.out.println(response);
//        String time = "2023-10-28 20:10:13";
//        String time1 = "2023-10-28 20:10:16";
//        int i = time.compareTo(time1);
//        int j = time1.compareTo(time);
//        System.out.println(i);
//        System.out.println(j);

    }


}
