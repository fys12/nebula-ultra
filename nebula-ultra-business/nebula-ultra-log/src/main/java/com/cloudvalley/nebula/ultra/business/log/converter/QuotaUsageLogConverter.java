package com.cloudvalley.nebula.ultra.business.log.converter;

import com.cloudvalley.nebula.ultra.business.log.model.entity.QuotaUsageLog;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.QuotaUsageLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotaUsageLogConverter {

    /**
     * 将 QuotaUsageLog 实体转换为 QuotaUsageLogVO 视图对象
     * @param quotaUsageLog 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 指定 TQuotaId 字段映射到 tQuotaId，处理字段名大小写不一致
     */
    @Mapping(source = "TQuotaId", target = "tQuotaId")
    QuotaUsageLogVO EnToVO(QuotaUsageLog quotaUsageLog);

    /**
     * 将 QuotaUsageLog 实体列表批量转换为 VO 列表
     * @param quotaUsageLogList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<QuotaUsageLogVO> EnListToVOList(List<QuotaUsageLog> quotaUsageLogList);

}
