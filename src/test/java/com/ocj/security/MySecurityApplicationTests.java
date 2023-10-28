package com.ocj.security;

import com.ocj.security.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class MySecurityApplicationTests {

    @Resource
    FileService fileService;
    @Test
    void contextLoads() {
        String preview = fileService.preview("hhuwh.png");

        System.out.println(preview);
    }

}
