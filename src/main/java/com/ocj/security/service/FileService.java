package com.ocj.security.service;

import com.ocj.security.commom.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    ResponseResult uploadFile(MultipartFile file);
}
