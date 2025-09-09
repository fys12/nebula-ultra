package com.cloudvalley.nebula.monolith.business.role.converter;

import com.cloudvalley.nebula.monolith.business.role.model.entity.RoleUser;
import com.cloudvalley.nebula.monolith.shared.api.role.model.vo.RoleUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleUserConverter {

    /**
     * 将 RoleUser 实体转换为 RoleUserVO 视图对象
     * @param roleUser 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 TUserId 映射到 tUserId、TRoleId 映射到 tRoleId，处理字段大小写不一致问题
     */
    @Mapping(source = "TUserId", target = "tUserId")
    @Mapping(source = "TRoleId", target = "tRoleId")
    RoleUserVO EnToVO(RoleUser roleUser);

    /**
     * 将 RoleUser 实体列表批量转换为 VO 列表
     * @param roleUserList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<RoleUserVO> EnListToVOList(List<RoleUser> roleUserList);
    
}
