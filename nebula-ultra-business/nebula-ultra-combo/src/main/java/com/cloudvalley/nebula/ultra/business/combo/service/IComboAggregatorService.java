package com.cloudvalley.nebula.ultra.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.combo.model.vo.ComboDetailsVO;

public interface IComboAggregatorService {

    /**
     * 获取租 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param current 当前页
     * @param size 每页数量
     * @return 套餐详情信息
     */
    IPage<ComboDetailsVO> getComboInfo(Integer current, Integer size);
}
