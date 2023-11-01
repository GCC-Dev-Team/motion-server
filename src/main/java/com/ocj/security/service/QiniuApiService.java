package com.ocj.security.service;

import com.qiniu.common.QiniuException;

public interface QiniuApiService {
    String TextCensor(String text)throws QiniuException;
    String ImageCensor(String imageUrl) throws QiniuException;

    String VideoCensor(String videoUrl) throws QiniuException;

    String getVideoCensorResultByJobID(String jobId);
}
