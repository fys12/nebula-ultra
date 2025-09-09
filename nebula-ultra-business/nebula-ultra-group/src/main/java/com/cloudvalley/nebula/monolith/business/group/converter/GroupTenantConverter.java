package com.cloudvalley.nebula.monolith.business.group.converter;

import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupTenant;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupTenantConverter {

    /**
     * 将 GroupTenant 实体转换为 GroupTenantVO 视图对象
     *
     * @param groupTenant 源实体对象
     * @return 转换后的 GroupTenantVO 对象
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SGroupId", target = "sGroupId")
    GroupTenantVO EnToVO(GroupTenant groupTenant);

    /**
     * 将 GroupTenant 实体列表批量转换为 GroupTenantVO 列表
     *
     * @param groupTenantList 源实体对象列表
     * @return 转换后的 GroupTenantVO 对象列表
     */
    List<GroupTenantVO> EnListToVOList(List<GroupTenant> groupTenantList);
    
}
