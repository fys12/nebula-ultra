package com.cloudvalley.nebula.monolith.business.quota.converter;

import com.cloudvalley.nebula.monolith.business.quota.model.entity.QuotaTenant;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.QuotaTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotaTenantConverter {

    /**
     * 将 QuotaTenant 实体转换为 QuotaTenantVO 视图对象
     * @param quotaTenant 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 STenantId 映射到 sTenantId、SQuotaId 映射到 sQuotaId，处理字段大小写不一致问题
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SQuotaId", target = "sQuotaId")
    QuotaTenantVO EnToVO(QuotaTenant quotaTenant);

    /**
     * 将 QuotaTenant 实体列表批量转换为 VO 列表
     * @param quotaTenantList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<QuotaTenantVO> EnListToVOList(List<QuotaTenant> quotaTenantList);
    
}
