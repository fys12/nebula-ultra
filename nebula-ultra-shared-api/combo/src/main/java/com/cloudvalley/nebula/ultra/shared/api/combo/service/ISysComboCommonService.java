package com.cloudvalley.nebula.ultra.shared.api.combo.service;


import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.SysComboVO;

import java.util.List;
import java.util.Map;

public interface ISysComboCommonService {

    /**
     * 根据ID查询系统套餐（单个）
     *
     * @param id 套餐ID
     * @return 套餐信息
     */
    SysComboVO getSysComboById(Long id);

    /**
     * 根据ID批量查询系统套餐 [全量]
     *
     * @param ids 套餐ID列表
     * @return 系统套餐信息
     */
    Map<Long, SysComboVO> getSysCombosByIds(List<Long> ids);

}
