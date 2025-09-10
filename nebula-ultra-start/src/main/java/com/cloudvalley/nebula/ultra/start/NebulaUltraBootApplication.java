package com.cloudvalley.nebula.ultra.start;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cloudvalley"})
@MapperScan({"com.cloudvalley.nebula.ultra.**.mapper"})
public class NebulaUltraBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(NebulaUltraBootApplication.class, args);
        System.out.println("项目启动成功");
    }
}
