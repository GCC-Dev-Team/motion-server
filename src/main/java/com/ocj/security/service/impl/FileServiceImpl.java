package com.ocj.security.service.impl;

import com.google.gson.Gson;
import com.ocj.security.config.QinuConfig;
import com.ocj.security.domain.vo.CoverVO;
import com.ocj.security.service.FileService;
import com.ocj.security.service.QiniuApiService;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@Service
public class FileServiceImpl implements FileService {
    @Resource
    QiniuApiService qiniuApiService;
    private static final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);


    /**
     * 上传文件的接口
     *
     * @param file
     * @param fileAddress 文件的路径，但是注意的是，如果是前面有文件夹，要在加入/
     * @return
     */
    @Override
    public String uploadFile(MultipartFile file, String fileAddress) {
        return qiniuApiService.uploadFile(file, fileAddress);
    }

    @Override
    public String preview(String fileAddress) {
        return qiniuApiService.preview(fileAddress);
    }


    @Override
    public Boolean processFile(String fileAddress, String persistentOpf, String saveAddress) {
        return qiniuApiService.processFile(fileAddress, persistentOpf, saveAddress);
    }


    @Override
    public CoverVO urlGetPhotoImage(String imageUrl) {

        try {
            System.out.println(imageUrl);
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            BufferedImage image = ImageIO.read(url);

            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();

                return new CoverVO(imageUrl, width, height);
            } else {
                log.info("无法读取图像");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

}
