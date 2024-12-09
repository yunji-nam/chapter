package com.example.chapter.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IamportConfig {

    @Value("${imp.restapi.key}")
    private String apiKey;
    @Value("${imp.api.secretKey}")
    private String secretKey;

    @Bean
    public IamportClient iamportClient() {
            return new IamportClient(apiKey, secretKey);
        }
}
