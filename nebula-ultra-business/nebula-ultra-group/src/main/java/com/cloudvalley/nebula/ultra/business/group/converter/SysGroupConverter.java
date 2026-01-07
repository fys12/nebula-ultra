package com.cloudvalley.nebula.ultra.business.group.converter;

import com.cloudvalley.nebula.ultra.business.group.model.entity.SysGroup;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.SysGroupVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysGroupConverter {

    /**
     * 将 SysGroup 实体转换为 SysGroupVO 视图对象
     *
     * @param sysGroup 源实体对象
     * @return 转换后的 SysGroupVO 对象
     */
    @Mapping(source = "GDesc", target = "desc")
    SysGroupVO EnToVO(SysGroup sysGroup);

    /**
     * 将 SysGroup 实体列表批量转换为 SysGroupVO 列表
     *
     * @param sysGroupList 源实体对象列表
     * @return 转换后的 SysGroupVO 对象列表
     */
    List<SysGroupVO> EnListToVOList(List<SysGroup> sysGroupList);

}
