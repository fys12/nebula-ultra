package com.cloudvalley.nebula.ultra.business.log.converter;

import com.cloudvalley.nebula.ultra.business.log.model.entity.ComboChangeLog;
import com.cloudvalley.nebula.ultra.shared.api.log.model.vo.ComboChangeLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboChangeLogConverter {

    /**
     * 将 ComboChangeLog 实体转换为 ComboChangeLogVO 视图对象
     * @param comboChangeLog 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 指定 STenantId 字段映射到 sTenantId，处理字段名大小写不一致
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    ComboChangeLogVO EnToVO(ComboChangeLog comboChangeLog);

    /**
     * 将 ComboChangeLog 实体列表批量转换为 VO 列表
     * @param comboChangeLogList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<ComboChangeLogVO> EnListToVOList(List<ComboChangeLog> comboChangeLogList);

}
