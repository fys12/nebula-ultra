package com.cloudvalley.nebula.monolith.business.log.converter;

import com.cloudvalley.nebula.monolith.business.log.model.entity.QuotaChangeLog;
import com.cloudvalley.nebula.monolith.shared.api.log.model.vo.QuotaChangeLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuotaChangeLogConverter {

    /**
     * 将 QuotaChangeLog 实体转换为 QuotaChangeLogVO 视图对象
     * @param quotaChangeLog 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 指定 TQuotaId 字段映射到 tQuotaId，处理字段名大小写不一致
     */
    @Mapping(source = "TQuotaId", target = "tQuotaId")
    QuotaChangeLogVO EnToVO(QuotaChangeLog quotaChangeLog);

    /**
     * 将 QuotaChangeLog 实体列表批量转换为 VO 列表
     * @param quotaChangeLogList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    @Mapping(source = "TQuotaId", target = "tQuotaId")
    List<QuotaChangeLogVO> EnListToVOList(List<QuotaChangeLog> quotaChangeLogList);

}
