package com.cloudvalley.nebula.ultra.business.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantDetailsVO;
import com.cloudvalley.nebula.ultra.business.tenant.model.vo.TenantSubscribeDetailsVO;

import java.util.List;

public interface ITenantAggregatorService {

    /**
     * 获取租户详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户详情信息
     */
    IPage<TenantDetailsVO> getTenantInfo(Integer current, Integer size);

    /**
     * 获取租户订阅（租户套餐）详情信息
     * 总体将 绑定的Id 转为实体数据
     * @param current 当前页
     * @param size 每页数量
     * @return 租户订阅（租户套餐）
     */
    IPage<TenantSubscribeDetailsVO> getTenantSubscribeInfo(Integer current, Integer size);
}
