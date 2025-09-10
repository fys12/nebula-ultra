package com.cloudvalley.nebula.ultra.core.config;

import cn.dev33.satoken.stp.StpLogic;
import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaTokenConfigure {

    // Sa-Token 整合 jwt (Simple 简单模式)
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

}
