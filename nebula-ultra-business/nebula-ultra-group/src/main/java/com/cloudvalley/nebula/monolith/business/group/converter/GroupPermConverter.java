package com.cloudvalley.nebula.monolith.business.group.converter;

import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupPerm;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupPermVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupPermConverter {

    /**
     * 将 GroupPerm 实体转换为 GroupPermVO 视图对象
     *
     * @param groupPerm 源实体对象
     * @return 转换后的 GroupPermVO 对象
     */
    @Mapping(source = "SGroupId", target = "sGroupId")
    @Mapping(source = "TPermId", target = "tPermId")
    GroupPermVO EnToVO(GroupPerm groupPerm);

    /**
     * 将 GroupPerm 实体列表批量转换为 GroupPermVO 列表
     *
     * @param groupPermList 源实体对象列表
     * @return 转换后的 GroupPermVO 对象列表
     */
    List<GroupPermVO> EnListToVOList(List<GroupPerm> groupPermList);
    
}
