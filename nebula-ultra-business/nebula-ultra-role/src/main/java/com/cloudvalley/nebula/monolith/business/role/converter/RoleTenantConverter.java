package com.cloudvalley.nebula.monolith.business.role.converter;

import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleTenant;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleTenantConverter {

    /**
     * 将 RoleTenant 实体转换为 RoleTenantVO 视图对象
     * @param roleTenant 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 STenantId 映射到 sTenantId、SRoleId 映射到 sRoleId，处理字段大小写不一致问题
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SRoleId", target = "sRoleId")
    RoleTenantVO EnToVO(RoleTenant roleTenant);

    /**
     * 将 RoleTenant 实体列表批量转换为 VO 列表
     * @param roleTenantList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<RoleTenantVO> EnListToVOList(List<RoleTenant> roleTenantList);
    
}
