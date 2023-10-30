package com.ocj.security.service.impl;

import com.google.gson.Gson;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.service.FileService;
import com.ocj.security.utils.FileUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.DownloadUrl;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.UploadPolicy;
import com.qiniu.util.Auth;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;


@Service
public class FileServiceImpl implements FileService {


    /**
     * 上传文件的接口
     * @param file
     * @param fileAddress 文件的路径，但是注意的是，如果是前面有文件夹，要在加入/
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file,String fileAddress) {
        try {

            byte[] bytes = file.getBytes();

            //构造一个带指定 Region 对象的配置类
            // Region.region2()表示华南地区，由于我们的对象存储是在华南，所以选择华南
            Configuration cfg = new Configuration(Region.region2());
            cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本


            UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
            String accessKey = "MAh90OZvgbaAY6m5g8DhtNC5s7TtFomeGHu2pCrT";
            String secretKey = "Zm2fQUO3zMIH7R8psNby8oCqywT8QshxcXjNc54A";
            String bucket = "motion1024";

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            Response response = uploadManager.put(bytes, fileAddress+"." +FileUtil.getFileExtension(file), upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);

            return preview(putRet.key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String preview(String fileAddress) {

        String domain="s36fh9xu3.hn-bkt.clouddn.com";

        return domain+"/"+fileAddress;
    }
}
