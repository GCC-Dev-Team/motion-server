package com.ocj.security.service;

import com.ocj.security.commom.ResponseResult;
import com.ocj.security.domain.vo.CoverVO;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    /**
     * 上传文件的接口
     * @param file
     * @param fileName 文件的路径，但是注意的是，如果是前面有文件夹，要在加入/
     * @return 文件的路径
     */
    String uploadFile(MultipartFile file,String prefix,String fileName);

    String preview(String fileAddress);


    CoverVO urlGetPhotoImage(String imageUrl);

    /**
     * 对文件的操作
     * @param fileAddress 需要的是上传的key
     * @param persistentOpf 操作指令
     * @return 舍弃使用
     */

    Boolean processFile(String fileAddress,String persistentOpf,String saveAddress);
}
