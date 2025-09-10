package com.cloudvalley.nebula.ultra.core.model.vo;

import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckTenantVO {

    /**
     * 有效租户 [ 系统级 ]
     */
    List<SysTenantVO> validSysTenant;

    /**
     * 禁用租户 [ 系统级 ]
     */
    List<SysTenantVO> disabledSysTenant;

    
}
