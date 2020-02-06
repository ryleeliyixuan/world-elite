package com.worldelite.job.context.config;

import lombok.Data;
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
}
