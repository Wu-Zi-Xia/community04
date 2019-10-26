package com.cduestc.community.community;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = "com.cduestc.community.community.mapper")//自动路由到我们的mapper文件，spring boot就可以扫描到mapper文件
public class CommunityApplication extends SpringBootServletInitializer {


    protected SpringApplicationBuilder configure(SpringApplicationBuilder application)
    {
        return application.sources(CommunityApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}
