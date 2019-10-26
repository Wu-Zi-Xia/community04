package com.cduestc.community.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.theme.ThemeChangeInterceptor;

@Configuration
//@EnableWebMvc//如果放开这个注解，那么就不会走默认的路径去加载静态文件，需要我们重写addResourceHandlers方法去定义静态资源的加载路径
public class WebConfig implements WebMvcConfigurer
{
    @Autowired
    SessionInterceptor sessionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    };

}
