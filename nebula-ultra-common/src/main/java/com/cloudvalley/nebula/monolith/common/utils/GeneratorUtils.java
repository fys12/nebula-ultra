package com.cloudvalley.nebula.monolith.common.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import java.time.LocalDateTime;

/**
 * 生成类
 */
public class GeneratorUtils {

    /**
     * 生成Id [雪花算法]
     * @return 分布式唯一ID (Long 类型，适合数据库 BIGINT)
     */
    public static Long generateId() {
        // 这里使用 MyBatis-Plus 内置的雪花算法实现
        return IdWorker.getId(); // 或 IdWorker.getId(SysUser.class) 等
    }

    /**
     * 生成当前时间
     */
    public static LocalDateTime generateCurrentTime() {
        return LocalDateTime.now();
    }
}
