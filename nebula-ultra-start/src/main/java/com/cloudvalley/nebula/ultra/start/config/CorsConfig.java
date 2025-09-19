package com.cloudvalley.nebula.ultra.start.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        // 允许的源，可以设置为前端的地址http://localhost:5173  http://47.109.76.80:1818
        config.addAllowedOrigin("http://localhost:5173");
//        config.addAllowedOriginPattern("http://localhost:5174");
//        config.addAllowedOriginPattern("http://localhost:5175");
//        config.addAllowedOriginPattern("http://192.168.216.13:5173");
        // 允许的请求头
        config.addAllowedHeader("*");
        // 允许的请求方法
        config.addAllowedMethod("*");
        //支持安全证书。跨域携带cookie需要配置这个
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        System.out.println("跨域配置被扫描");
        return new CorsFilter(source);
    }

}
