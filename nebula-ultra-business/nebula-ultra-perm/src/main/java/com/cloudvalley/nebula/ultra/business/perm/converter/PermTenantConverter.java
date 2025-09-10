package com.cloudvalley.nebula.ultra.business.perm.converter;

import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermTenant;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermTenantConverter {

    /**
     * 将 PermTenant 实体转换为 PermTenantVO 视图对象
     * @param permTenant 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 STenantId 映射到 sTenantId、SPermId 映射到 sPermId，处理字段大小写不一致问题
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SPermId", target = "sPermId")
    PermTenantVO EnToVO(PermTenant permTenant);

    /**
     * 将 PermTenant 实体列表批量转换为 VO 列表
     * @param permTenantList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<PermTenantVO> EnListToVOList(List<PermTenant> permTenantList);

}
