package com.ocj.security.service;

import com.qiniu.common.QiniuException;
import org.springframework.web.multipart.MultipartFile;

public interface QiniuApiService {
    /**文本审核
     * @param text
     * @return
     * @throws QiniuException
     */
    String TextCensor(String text)throws QiniuException;

    /**
     * 图片审核
     * @param imageUrl
     * @return
     * @throws QiniuException
     */
    String ImageCensor(String imageUrl) throws QiniuException;

    /**
     * 视频审核
     * @param videoUrl
     * @return
     * @throws QiniuException
     */
    String VideoCensor(String videoUrl) throws QiniuException;

    String getVideoCensorResultByJobID(String jobId);

    /**
     * 上传文件的接口，
     * @param file 文件
     * @param fileAddress 文件的地址，需要目录记得携带目录
     * @return
     */
    String uploadFile(MultipartFile file, String fileAddress);

    /**
     * 展示图片的接口（提供地址就行）注意这个有目录记得加上去
     * @param fileAddress
     * @return
     */
    String preview(String fileAddress);

    /**
     * 处理已经上传的视频
     * @param fileAddress 文件地址（注意带上目录）
     * @param persistentOpf 操作指令
     * @param saveAddress 保存的地址，有目录请注意带上
     * @return
     */
    Boolean processFile(String fileAddress, String persistentOpf, String saveAddress);

}
