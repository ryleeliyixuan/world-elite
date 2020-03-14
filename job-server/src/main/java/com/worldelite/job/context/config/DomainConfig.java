package com.worldelite.job.context.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author yeguozhong yedaxia.github.com
 */
@Data
@Component
public class DomainConfig {

    @Value("${domain.oss}")
    private String oss;

    @Value("${domain.inner.oss}")
    private String innerOss;

    @Value("${domain.web.host}")
    private String webHost;

    @Value("${server.port}")
    private String serverPort;

    public String getLocalHost(){
        return "http://127.0.0.1:"+ serverPort;
    }
}
