package com.cloudvalley.nebula.ultra.shared.api.quoat.service;

import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;

import java.util.List;
import java.util.Map;

public interface ISysQuotaCommonService {

    /**
     * 根据id查询系统配额
     * @param id 系统配额ID
     * @return 系统配额信息
     */
    SysQuotaVO getSysQuotaById(Long id);

    /**
     * 根据id查询系统配额(可查询单个或多个，传入的id可以单个或多个)[全量]
     * @param ids 系统配额ID列表
     * @return 系统配额信息
     */
    List<SysQuotaVO> getSysQuotasByIds(List<Long> ids);


}
