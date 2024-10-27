package com.example.backend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.example.backend.models.*;
import com.example.backend.repositories.*;

import javax.persistence.Entity;
import java.util.Map;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.backend.repositories")
@EntityScan(basePackages = "com.example.backend.models")
public class BackendApplication {
	private static final Logger logger = LoggerFactory.getLogger(BackendApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(BackendApplication.class, args);
// 打印所有扫描到的实体类，使用 logger 而不是 System.out
		Map<String, Object> entities = context.getBeansWithAnnotation(Entity.class);
		logger.info("已扫描的实体类：");
		for (String entityName : entities.keySet()) {
			logger.info("Entity: " + entityName);
		}
	}
}
