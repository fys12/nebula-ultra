package com.cloudvalley.nebula.monolith.business.tenant.converter;

import com.cloudvalley.nebula.monolith.business.tenant.model.entity.TenantSubscribe;
import com.cloudvalley.nebula.monolith.shared.api.tenant.model.vo.TenantSubscribeVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantSubscribeConverter {

    /**
     * 将 TenantSubscribe 实体转换为 TenantSubscribeVO 视图对象
     * @param tenantSubscribe 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 STenantId 映射到 sTenantId、SComboId 映射到 sComboId，处理字段大小写不一致问题
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SComboId", target = "sComboId")
    TenantSubscribeVO EnToVO(TenantSubscribe tenantSubscribe);

    /**
     * 将 TenantSubscribe 实体列表批量转换为 VO 列表
     * @param tenantSubscribeList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<TenantSubscribeVO> EnListToVOList(List<TenantSubscribe> tenantSubscribeList);

}
