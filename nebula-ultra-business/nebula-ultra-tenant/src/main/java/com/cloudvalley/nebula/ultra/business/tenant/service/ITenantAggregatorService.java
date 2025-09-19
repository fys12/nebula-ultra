package com.cloudvalley.nebula.ultra.business.tenant.service;

import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantDetailsVO;

import java.util.List;

public interface ITenantAggregatorService {

    /**
     * 获取租户详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户详情信息
     */
    List<TenantDetailsVO> getTenantInfo(Integer current, Integer size);
}
