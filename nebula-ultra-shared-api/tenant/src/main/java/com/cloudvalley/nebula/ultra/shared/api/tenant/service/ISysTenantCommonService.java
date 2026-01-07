package com.cloudvalley.nebula.ultra.shared.api.tenant.service;

import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;

import java.util.List;
import java.util.Map;

public interface ISysTenantCommonService {

    /**
     * 根据id查询系统租户
     * @param id 系统租户ID
     * @return 系统租户信息
     */
    SysTenantVO getSysTenantById(Long id);

    /**
     * 根据多个系统租户ID全量查询租户信息（不分页）
     * @param ids 系统租户ID列表
     * @return 所有匹配的 SysTenantVO
     */
    List<SysTenantVO> getSysTenantsByIds(List<Long> ids);

    /**
     * 查询 所有 系统租户信息
     * @return 系统租户列表
     */
    List<SysTenantVO> getSysTenants();


}
