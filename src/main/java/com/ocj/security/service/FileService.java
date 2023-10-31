package com.ocj.security.service;

import com.ocj.security.commom.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件的接口
     * @param file
     * @param fileAddress 文件的路径，但是注意的是，如果是前面有文件夹，要在加入/
     * @return 文件的路径
     */
    String uploadFile(MultipartFile file,String fileAddress);

    String preview(String fileAddress);

//    void

    /**
     * 对文件的操作
     * @param fileAddress 需要的是上传的key
     * @param persistentOpf 操作指令
     * @return
     */

    Boolean processFile(String fileAddress,String persistentOpf,String saveAddress);
}
