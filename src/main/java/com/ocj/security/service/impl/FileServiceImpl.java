package com.ocj.security.service.impl;

import com.google.gson.Gson;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.service.FileService;
import com.ocj.security.utils.FileUtil;
import com.qiniu.common.QiniuException;
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


@Service
public class FileServiceImpl implements FileService {
    @Resource
    QinuConfig qinuConfig;


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

            Auth auth = Auth.create(qinuConfig.getAccessKey(), qinuConfig.getSecretKey());
            String upToken = auth.uploadToken(qinuConfig.getBucket());

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

        String domain=qinuConfig.getDomain();

        return domain+"/"+fileAddress;
    }


    @Override
    public Boolean processFile(String fileAddress, String persistentOpf, String saveAddress) {
       //获取鉴权
        Auth auth = Auth.create(qinuConfig.getAccessKey(), qinuConfig.getSecretKey());
        Configuration cfg = new Configuration(Region.region0());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本

        //新建一个OperationManager对象
        OperationManager operater = new OperationManager(auth,cfg);

        //设置保存的路径
        //可以对转码后的文件进行使用saveas参数自定义命名，当然也可以不指定文件会默认命名并保存在当前空间。
        String urlbase64 = UrlSafeBase64.encodeToString("motion1024:"+saveAddress);
        String pfops = persistentOpf + "|saveas/" + urlbase64;

        //设置pipeline参数
        StringMap params = new StringMap().putWhen("force", 1, true).putNotEmpty("pipeline", null);
        try {
          operater.pfop(qinuConfig.getBucket(), fileAddress, pfops, params);

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
