package com.cloudvalley.nebula.ultra.business.group.converter;

import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindDept;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupBindDeptConverter {

    /**
     * 将 GroupBindDept 实体转换为 VO
     * @param groupBindDept 要转换的实体对象
     * @return 转换后的 VO 对象
     */
    @Mapping(source = "SGroupId", target = "sGroupId")
    @Mapping(source = "TDeptId", target = "tDeptId")
    GroupBindDeptVO EnToVO(GroupBindDept groupBindDept);

    /**
     * 将 GroupBindDept 实体列表批量转换为 VO 列表
     * @param groupBindDeptList 要转换的实体对象列表
     * @return 转换后的 VO 对象列表
     */
    List<GroupBindDeptVO> EnListToVOList(List<GroupBindDept> groupBindDeptList);

}
