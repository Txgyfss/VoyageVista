package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final Log log = LogFactory.getLog(WebConfig.class);


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 正确映射静态资源路径
        String resourcePath = "classpath:/static/";
        String resourceUrl = "/uploads/**";

        registry.addResourceHandler(resourceUrl)
                .addResourceLocations(resourcePath);

        // 输出映射路径
        log.info("Mapped URL path [" + resourceUrl + "] to location [" + resourcePath + "]");
    }


    private void printActualResourcePath(String resourcePath) {
        // 获取实际路径
        ClassLoader classLoader = getClass().getClassLoader();
        URL resourceUrl = classLoader.getResource(resourcePath);
        if (resourceUrl != null) {
            log.info("Actual resource path: " + resourceUrl.getFile());
        } else {
            log.warn("Resource path not found: " + resourcePath);
        }
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 通用CORS配置
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);

        // 针对静态资源的CORS配置
        registry.addMapping("/uploads/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}