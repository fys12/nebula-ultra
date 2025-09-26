package com.cloudvalley.nebula.ultra.business.quota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.quota.model.vo.QuoatDetailsVO;

public interface IQuotaAggregatorService {

    /**
     * 获取租户 配额 信息
     * @param current 当前页
     * @param size 每页数量
     * @return 租户配额信息
     */
    IPage<QuoatDetailsVO> getTenantQuotaInfo(Integer current, Integer size);
}
