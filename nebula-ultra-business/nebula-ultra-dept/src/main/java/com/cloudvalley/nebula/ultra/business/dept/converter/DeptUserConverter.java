package com.cloudvalley.nebula.ultra.business.dept.converter;

import com.cloudvalley.nebula.ultra.business.dept.model.entity.DeptUser;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeptUserConverter {

    /**
     * 将用户-部门关联实体类（DeptUser）转换为视图对象（DeptUserVO）
     * 核心映射规则：实体类中大写开头的关联字段与VO中小写开头的对应字段映射，
     * 即实体的TUserId（租户用户ID）映射到VO的tUserId，实体的TDeptId（租户部门ID）映射到VO的tDeptId；
     * 其他字段名一致的属性（如id、createdAt、state、deleted等）将自动映射，无需额外配置
     * @param userDept 待转换的用户-部门关联实体对象（包含用户与部门关联的完整业务数据，如关联ID、创建时间、启用状态等）
     * @return DeptUserVO 转换后的用户-部门关联视图对象（用于接口返回，屏蔽实体层无关字段，适配前端数据展示或下游服务数据交互需求）
     */
    @Mapping(source = "TUserId", target = "tUserId")
    @Mapping(source = "TDeptId", target = "tDeptId")
    DeptUserVO EnToVO(DeptUser userDept);

    /**
     * 将用户-部门关联实体类列表（List<DeptUser>）批量转换为视图对象列表（List<DeptUserVO>）
     * 内部通过循环调用单个实体转换方法（EnToVO）实现批量处理，保证列表中每个实体与VO的映射规则一致，
     * 同时保持原列表的数据顺序和数量对应关系
     * @param userDeptList 待转换的用户-部门关联实体列表（可来自分页查询结果、批量查询结果等场景，包含多个关联实体数据）
     * @return List<DeptUserVO> 批量转换后的用户-部门关联视图列表（适用于批量数据展示，如用户部门关联列表页、批量数据导出等场景）
     */
    List<DeptUserVO> EnListToVOList(List<DeptUser> userDeptList);

}
