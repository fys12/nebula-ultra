package com.cloudvalley.nebula.ultra.business.tenant.converter;

import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysTenantConverter {

    /**
     * 将 SysTenant 实体转换为 SysTenantVO 视图对象
     * @param sysTenant 源实体对象
     * @return 转换后的 VO 对象
     */
    SysTenantVO EnToVO(SysTenant sysTenant);

    /**
     * 将 SysTenant 实体列表批量转换为 VO 列表
     * @param sysTenantList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<SysTenantVO> EnListToVOList(List<SysTenant> sysTenantList);

}
