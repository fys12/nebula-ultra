package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;

public interface IGroupAggregatorService {

    /**
     * 获取 权限组 详细 详细
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 详细 详细
     */
    IPage<GroupPermDetailsVO> getPermGroupInfo(Integer current, Integer size);
}
