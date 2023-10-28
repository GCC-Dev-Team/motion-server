package com.ocj.security.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUtil {
    public static String getFileExtension(MultipartFile file) {
        // 获取文件名
        String originalFilename = file.getOriginalFilename();

        // 检查文件名是否为空
        if (!StringUtils.isEmpty(originalFilename)) {
            // 获取文件后缀
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex >= 0) {
                return originalFilename.substring(dotIndex + 1);
            }
        }

        // 如果文件名为空或没有后缀，可以根据需要返回默认值，如空字符串或null。
        return null;
    }
}
