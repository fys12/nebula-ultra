package com.cloudvalley.nebula.monolith.business.combo.converter;

import com.cloudvalley.nebula.monolith.business.combo.model.entity.SysCombo;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.SysComboVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysComboConverter {

    /**
     * 将SysCombo实体对象转换为SysComboVO视图对象
     * 用于数据传输和展示，实现实体与视图模型之间的转换
     *
     * @param sysCombo 源实体对象，包含系统套餐的原始数据信息
     * @return 转换后的SysComboVO对象，用于前端展示或接口返回
     */
    @Mapping(source = "CDesc", target = "desc")
    SysComboVO EnToVO(SysCombo sysCombo);

    /**
     * 将SysCombo实体对象列表批量转换为SysComboVO视图对象列表
     * 批量处理实体到视图模型的转换，保持数据结构一致性
     *
     * @param sysComboList 源实体实体对象列表，包含多个多个系统套餐的原始数据信息
     * @return 转换后的SysComboVO对象列表，用于批量数据的前端展示或接口返回
     */
    List<SysComboVO> EnListToVOList(List<SysCombo> sysComboList);

}
