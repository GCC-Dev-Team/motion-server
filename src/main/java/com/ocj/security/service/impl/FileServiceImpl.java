package com.ocj.security.service.impl;

import com.google.gson.Gson;
import com.ocj.security.commom.ResponseResult;
import com.ocj.security.service.FileService;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public ResponseResult uploadFile(MultipartFile file) {
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

//默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = null;

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(bytes, "test", upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);

                return ResponseResult.okResult(response);
            } catch (QiniuException ex) {
                ex.printStackTrace();
                if (ex.response != null) {
                    System.err.println(ex.response);

                    try {
                        String body = ex.response.toString();
                        System.err.println(body);
                    } catch (Exception ignored) {
                    }
                }
            }

            return null;


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
