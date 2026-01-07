package com.cloudvalley.nebula.ultra.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.combo.model.vo.ComboDetailsVO;

public interface IComboAggregatorService {

    /**
     * 获取 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param comboId 套餐ID
     * @return 套餐详情信息
     */
    ComboDetailsVO getComboDetails(Long comboId);
}
