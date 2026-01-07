package com.cloudvalley.nebula.ultra.business.perm.converter;

import com.cloudvalley.nebula.ultra.business.perm.model.entity.PermUser;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.PermUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermUserConverter {

    /**
     * 将 PermUser 实体转换为 PermUserVO 视图对象
     * @param permUser 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 TUserId 映射到 tUserId、TPermId 映射到 tPermId，处理字段大小写不一致问题
     */
    @Mapping(source = "TUserId", target = "tUserId")
    @Mapping(source = "TPermId", target = "tPermId")
    PermUserVO EnToVO(PermUser permUser);

    /**
     * 将 PermUser 实体列表批量转换为 VO 列表
     * @param permUserList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<PermUserVO> EnListToVOList(List<PermUser> permUserList);

}
