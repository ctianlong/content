package com.netease.homework.content;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.netease.homework.content.mapper")
public class ContentApplication implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(ContentApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ContentApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        logger.info("服务启动");
    }
}

