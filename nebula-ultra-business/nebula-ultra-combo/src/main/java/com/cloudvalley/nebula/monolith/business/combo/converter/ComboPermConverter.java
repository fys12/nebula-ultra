package com.cloudvalley.nebula.monolith.business.combo.converter;

import com.cloudvalley.nebula.monolith.business.combo.model.entity.ComboPerm;
import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboPermVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboPermConverter {

    /**
     * 将套餐-权限绑定实体（ComboPerm）转换为视图对象（ComboPermVO）
     *
     * @param comboPerm 待转换的套餐-权限绑定实体，包含数据库层面的绑定关系数据
     * @return 转换后的套餐-权限绑定视图对象，用于对外展示绑定关系信息
     * @note 明确指定实体中大写开头的SComboId、SPermId字段，映射到VO中对应的小写字段
     */
    @Mapping(source = "SComboId", target = "sComboId")
    @Mapping(source = "SPermId", target = "sPermId")
    ComboPermVO EnToVO(ComboPerm comboPerm);

    /**
     * 将套餐-权限绑定实体列表（List<ComboPerm>）转换为视图对象列表（List<ComboPermVO>）
     *
     * @param comboPermList 待转换的套餐-权限绑定实体列表
     * @return 转换后的套餐-权限绑定视图对象列表，用于批量对外展示绑定关系信息
     * @note 内部默认调用EnToVO方法实现单个实体到VO的转换，保持字段映射规则一致
     */
    List<ComboPermVO> EnListToVOList(List<ComboPerm> comboPermList);

}
