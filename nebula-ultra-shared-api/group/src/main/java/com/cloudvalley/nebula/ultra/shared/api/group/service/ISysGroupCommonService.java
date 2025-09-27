package com.cloudvalley.nebula.ultra.shared.api.group.service;


import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;

import java.util.List;

public interface ISysGroupCommonService {

    /**
     * 根据系统组ID查询系统组（单个）
     * @param id 系统组ID
     * @return 系统组信息
     */
    SysGroupVO getSysGroupById(Long id);

    /**
     * 根据系统组ID批量查询系统组 [全量]
     * @param ids 系统组ID列表
     * @return 系统组信息
     */
    List<SysGroupVO> getSysGroupsByIds(List<Long> ids);

}
