package com.cloudvalley.nebula.ultra.core.auth;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码
     * @param userId 用户Id
     * @param loginType 登录类型
     * @return 有效权限码列表
     */
    @Override
    public List<String> getPermissionList(Object userId, String loginType) {
        return List.of();
    }

    /**
     * 返回一个账号所拥有的角色标识
     * @param userId 用户Id
     * @param loginType 登录类型
     * @return 有效角色标识列表
     */
    @Override
    public List<String> getRoleList(Object userId, String loginType) {
        return List.of();
    }
}
