package com.cloudvalley.nebula.monolith.shared.api.group.service;


import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.SysGroupVO;

public interface ISysGroupCommonService {

    /**
     * 根据系统组ID查询系统组（单个）
     * @param id 系统组ID
     * @return 系统组信息
     */
    SysGroupVO getSysGroupById(Long id);

}
