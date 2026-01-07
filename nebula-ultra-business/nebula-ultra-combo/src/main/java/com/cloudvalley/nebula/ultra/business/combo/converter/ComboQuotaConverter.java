package com.cloudvalley.nebula.ultra.business.combo.converter;

import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboQuota;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboQuotaVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ComboQuotaConverter {

    /**
     * 实体对象转VO对象映射方法
     * 将ComboQuota实体对象转换为ComboQuotaVO视图对象
     *
     * @param comboQuota 源实体对象，包含原始数据
     * @return 转换后的ComboQuotaVO对象，用于前端展示或接口返回
     * @see ComboQuota 源实体类
     * @see ComboQuotaVO 目标VO类
     */
    @Mapping(source = "SComboId", target = "sComboId")
    @Mapping(source = "SQuotaId", target = "sQuotaId")
    ComboQuotaVO EnToVO(ComboQuota comboQuota);

    /**
     * 实体列表转VO列表映射方法
     * 将ComboQuota实体对象列表批量转换为ComboQuotaVO视图对象列表
     *
     * @param comboQuotaList 源实体对象列表，包含多个原始数据对象
     * @return 转换后的ComboQuotaVO对象列表，用于批量数据的前端展示或接口返回
     * @see ComboQuota 源实体类
     * @see ComboQuotaVO 目标VO类
     */
    List<ComboQuotaVO> EnListToVOList(List<ComboQuota> comboQuotaList);

}
