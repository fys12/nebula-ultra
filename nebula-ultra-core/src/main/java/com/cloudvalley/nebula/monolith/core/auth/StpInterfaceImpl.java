package com.cloudvalley.nebula.monolith.core.auth;

import cn.dev33.satoken.stp.StpInterface;

import java.util.List;

public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object o, String s) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object o, String s) {
        return List.of();
    }
}
