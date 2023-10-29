package com.ocj.security.utils;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileToMultipartFileConverter {

    public static MultipartFile convertFileToMultipartFile(File file) throws IOException {
        FileInputStream input = new FileInputStream(file);
        byte[] fileBytes = new byte[(int) file.length()];
        input.read(fileBytes);

        ByteArrayResource resource = new ByteArrayResource(fileBytes);

        return new MockMultipartFile(file.getName(), file.getName(), "application/octet-stream", resource.getByteArray());
    }
}
