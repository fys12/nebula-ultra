package com.cloudvalley.nebula.monolith.business.combo.converter;

import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboRole;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboRoleConverter {

    /**
     * 将ComboRole实体对象转换为ComboRoleVO视图对象
     * 主要用于数据传输和展示，处理字段命名规范转换（如SComboId -> sComboId）
     *
     * @param comboRole 源实体对象，包含套餐与角色的绑定关系原始数据
     * @return 转换后的ComboRoleVO对象，用于前端展示或接口返回
     */
    @Mapping(source = "SComboId", target = "sComboId")
    @Mapping(source = "SRoleId", target = "sRoleId")
    ComboRoleVO EnToVO(ComboRole comboRole);

    /**
     * 将ComboRole实体对象列表批量转换为ComboRoleVO视图对象列表
     * 批量处理实体到VO的转换，保持数据结构一致性
     *
     * @param comboRoleList 源实体对象列表，包含多个套餐与角色的绑定关系原始数据
     * @return 转换后的ComboRoleVO对象列表，用于批量数据的前端展示或接口返回
     */
    List<ComboRoleVO> EnListToVOList(List<ComboRole> comboRoleList);

}
