package com.cloudvalley.nebula.monolith.business.perm.converter;

import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermRole;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermRoleVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermRoleConverter {

    /**
     * 将 PermRole 实体转换为 PermRoleVO 视图对象
     * @param permRole 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 TPermId 映射到 tPermId、TRoleId 映射到 tRoleId，处理字段大小写不一致问题
     */
    @Mapping(source = "TPermId", target = "tPermId")
    @Mapping(source = "TRoleId", target = "tRoleId")
    PermRoleVO EnToVO(PermRole permRole);

    /**
     * 将 PermRole 实体列表批量转换为 VO 列表
     * @param permRoleList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<PermRoleVO> EnListToVOList(List<PermRole> permRoleList);

}
