package com.ocj.security.config;

import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
public class QinuConfig {
    /**
     * 七牛云的ak
     */
    @Value("${quin.accessKey}")
    private String accessKey;

    /**
     * 七牛云的sk
     */
    @Value("${quin.secretKey}")
    private String secretKey;
    /**
     * 桶名称
     */
    @Value("${quin.bucket}")
    private String bucket;

    @Value("${quin.domain}")
    private String domain;

    @Bean
    public Auth auth(){
        return Auth.create(accessKey,secretKey);
    }



}
