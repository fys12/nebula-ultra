package com.cloudvalley.nebula.monolith.business.perm.converter;

import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermDept;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermDeptVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PermDeptConverter {

    /**
     * 将 PermDept 实体转换为 PermDeptVO 视图对象
     * @param permDept 源实体对象
     * @return 转换后的 VO 对象
     * @Mapping 分别将 TPermId 映射到 tPermId、TDeptId 映射到 tDeptId，处理字段大小写不一致问题
     */
    @Mapping(source = "TPermId", target = "tPermId")
    @Mapping(source = "TDeptId", target = "tDeptId")
    PermDeptVO EnToVO(PermDept permDept);

    /**
     * 将 PermDept 实体列表批量转换为 VO 列表
     * @param permDeptList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<PermDeptVO> EnListToVOList(List<PermDept> permDeptList);

}
