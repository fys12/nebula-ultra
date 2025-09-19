package com.cloudvalley.nebula.ultra.business.user.model.dto;

import lombok.Data;

@Data
public class LoginDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码（加密）
     */
    private String passwordHash;

    /**
     * 记住我模式（30天自动登录）
     */
    private Boolean rememberMe;

    /**
     * 7天免登录模式
     */
    private Boolean sevenDaysAutoLogin;
}
