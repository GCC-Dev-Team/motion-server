package com.ocj.security.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class FileUtil {
    /**
     * 获取文件后缀名
     *
     * @param file
     * @return
     */
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

    public static MultipartFile FileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        input.read(fileBytes);

        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        return new MockMultipartFile(file.getName(), file.getName(), "application/octet-stream", resource.getByteArray());
    }

    public static File MultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

    public static Boolean FileDelete(File file) {
        return file.delete();
    }
}
