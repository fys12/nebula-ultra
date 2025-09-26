package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupAggregatorService;
import org.springframework.stereotype.Service;

@Service
public class GroupAggregatorServiceImpl implements IGroupAggregatorService {

    /**
     * 获取 权限组 详细 详细
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 详细 详细
     */
    @Override
    public IPage<GroupPermDetailsVO> getPermGroupInfo(Integer current, Integer size) {
        // 1. 查询 权限组 基本信息

        // 1.2 获取 系统组Id

        // 1.3 获取 租户权限Id

        // 2. 根据 系统组Id 获取 系统组信息

        // 3. 根据 租户权限Id 获取 租户权限信息

        // 3.1 获取 系统权限Id

        // 3.2 根据 系统权限Id 获取 系统权限信息

        // 4. 组装数据
    }
}
