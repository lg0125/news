package com.chris.news.wemedia.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.chris.news.feignapi.article.fallback")
public class InitConfig {
}
