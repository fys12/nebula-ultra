package com.cloudvalley.nebula.ultra.business.group.converter;

import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindRole;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupBindRoleConverter {

    /**
     * 将 GroupBindRole 实体转换为 GroupBindRoleVO 视图对象
     *
     * @param groupBindRole 源实体对象
     * @return 转换后的 GroupBindRoleVO 对象
     */
    @Mapping(source = "TGroupId", target = "tGroupId")
    @Mapping(source = "TRoleId", target = "tRoleId")
    GroupBindRoleVO EnToVO(GroupBindRole groupBindRole);

    /**
     * 将 GroupBindRole 实体列表批量转换为 GroupBindRoleVO 列表
     *
     * @param groupBindRoleList 源实体对象列表
     * @return 转换后的 GroupBindRoleVO 对象列表
     */
    List<GroupBindRoleVO> EnListToVOList(List<GroupBindRole> groupBindRoleList);
    
}
