package com.ocj.security.service.impl;

import com.google.gson.Gson;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.service.QiniuApiService;
import com.ocj.security.utils.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Client;
import com.qiniu.http.Response;
import com.qiniu.processing.OperationManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import com.qiniu.util.UrlSafeBase64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class QiniuApiServiceImpl implements QiniuApiService {

    @Resource
    QinuConfig qinuConfig;

    @Resource
    Auth auth;

    private final Client client = new Client();

    @Override
    public String TextCensor(String text) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map<String, Object> uri = new HashMap<>();
        uri.put("text", text);

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"antispam"};
        scenes.put("scenes", types);

        Map params = new HashMap();
        params.put("data", uri);
        params.put("params", scenes);

        String paraR = gson.toJson(params);
        byte[] bodyByte = paraR.getBytes();

        // 接口请求地址//http://ai.qiniuapi.com/v3/text/censor
        String url = "http://ai.qiniuapi.com/v3/text/censor";

        return post(url, bodyByte);
    }


    //参考api文档 https://developer.qiniu.com/dora/manual/4252/image-review
    //图片审核
    @Override
    public String ImageCensor(String imageUrl) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map<String, Object> uri = new HashMap<>();
        uri.put("uri", imageUrl);

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"pulp", "terror", "politician", "ads"};
        scenes.put("scenes", types);

        Map params = new HashMap();
        params.put("data", uri);
        params.put("params", scenes);

        String paraR = gson.toJson(params);

        //注意要指定编码格式 否则使用中文会报 badtoken错误
        byte[] bodyByte = paraR.getBytes(StandardCharsets.UTF_8);

        // 接口请求地址
        String url = "http://ai.qiniuapi.com/v3/image/censor";

        return post(url, bodyByte);
    }

    //参考api文档 https://developer.qiniu.com/dora/manual/4258/video-pulp
    //视频审核
    @Override
    public String VideoCensor(String videoUrl) throws QiniuException {
        // 构造post请求body
        Gson gson = new Gson();

        Map bodyData = new HashMap();

        Map<String, Object> uri = new HashMap<>();
        uri.put("uri", videoUrl);

        Map<String, Object> params = new HashMap<>();

        Map<String, Object> scenes = new HashMap<>();
        //pulp 黄  terror 恐  politician 敏感人物
        String[] types = {"pulp", "terror", "politician"};

        Map<String, Object> cut_param = new HashMap<>();
        cut_param.put("interval_msecs", 500);

        params.put("scenes", types);
        params.put("cut_param", cut_param);

        bodyData.put("data", uri);
        bodyData.put("params", params);
        String paraR = gson.toJson(bodyData);
        byte[] bodyByte = paraR.getBytes(StandardCharsets.UTF_8);

        // 接口请求地址
        String url = "http://ai.qiniuapi.com/v3/video/censor";
        return post(url, bodyByte);
    }

    /**
     * 查询视频审核内容结果
     * 参考
     * https://developer.qiniu.com/censor/api/5620/video-censor#4
     *
     * @param jobId : 视频审核返回的 job ID
     */
    public String getVideoCensorResultByJobID(String jobId) {
        String url = "http://ai.qiniuapi.com/v3/jobs/video/".concat(jobId);

        String accessToken = (String) auth.authorizationV2(url).get("Authorization");
        StringMap headers = new StringMap();
        headers.put("Authorization", accessToken);

        try {
            com.qiniu.http.Response resp = client.get(url, headers);
            return resp.bodyString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送post请求
     * @param url
     * @param body
     * @return
     * @throws QiniuException
     */
    private String post(String url, byte[] body) throws QiniuException {
        com.qiniu.http.Response resp = client.post(url, body, auth.authorizationV2(url, "POST", body, "application/json"), Client.JsonMime);
        return resp.bodyString();
    }


    @Override
    public String uploadFile(MultipartFile file, String fileAddress) {
        try {

            byte[] bytes = file.getBytes();

            //构造一个带指定 Region 对象的配置类
            // Region.region2()表示华南地区，由于我们的对象存储是在华南，所以选择华南
            Configuration cfg = new Configuration(Region.region2());
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
            UploadManager uploadManager = new UploadManager(cfg);

            String upToken = auth.uploadToken(qinuConfig.getBucket());
            Response response = uploadManager.put(bytes, fileAddress + "." + FileUtil.getFileExtension(file), upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            return preview(putRet.key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String preview(String fileAddress) {

        String domain=qinuConfig.getDomain();

        return domain+"/"+fileAddress;
    }

    @Override
    public Boolean processFile(String fileAddress, String persistentOpf, String saveAddress) {

        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth,cfg);

        //设置保存的路径
        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString("motion-prod:"+saveAddress);
        String pfops = persistentOpf + "|saveas/" + urlbase64;

        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", null);
        try {
            String pfop = operater.pfop(qinuConfig.getBucket(), fileAddress, pfops, params);
            System.out.println(pfop);

        } catch (QiniuException e) {
            //捕获异常信息
            Response r = e.response;
            // 请求失败时简单状态信息
            System.out.println(r.toString());
            try {
                // 响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException ignored) {

            }
        }
        return Boolean.TRUE;
    }
}
