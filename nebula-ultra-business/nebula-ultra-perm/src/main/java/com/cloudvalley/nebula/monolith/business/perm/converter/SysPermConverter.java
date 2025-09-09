package com.cloudvalley.nebula.monolith.business.perm.converter;

import com.cloudvalley.nebula.monolith.business.perm.model.entity.SysPerm;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.SysPermVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysPermConverter {

    /**
     * 将 SysPerm 实体转换为 SysPermVO 视图对象
     * @param sysPerm 源实体对象
     * @return 转换后的 VO 对象
     */
    SysPermVO EnToVO(SysPerm sysPerm);

    /**
     * 将 SysPerm 实体列表批量转换为 VO 列表
     * @param sysPermList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<SysPermVO> EnListToVOList(List<SysPerm> sysPermList);

}
