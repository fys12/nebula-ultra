package com.cloudvalley.nebula.ultra.business.dept.converter;

import com.cloudvalley.nebula.ultra.business.dept.model.entity.DeptTenant;
import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeptTenantConverter {

    /**
     * 将租户-部门绑定实体类（DeptTenant）转换为视图对象（DeptTenantVO）
     * 处理字段映射：实体类中的STenantId（系统租户ID）映射到VO的sTenantId，实体类中的SDeptId（系统部门ID）映射到VO的sDeptId
     * 其他名称一致的字段将自动映射，无需额外配置
     * @param tenantDept 待转换的租户-部门绑定实体对象（包含主键ID、租户ID、部门ID、创建时间、启用状态等完整实体信息）
     * @return DeptTenantVO 转换后的租户-部门绑定视图对象（用于接口返回，隐藏实体层不必要字段，适配前端展示需求）
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SDeptId", target = "sDeptId")
    DeptTenantVO EnToVO(DeptTenant tenantDept);

    /**
     * 将租户-部门绑定实体类列表（List<DeptTenant>）批量转换为视图对象列表（List<DeptTenantVO>）
     * 内部会循环调用EnToVO方法处理单个实体到VO的转换，保持列表数据结构和元素对应关系
     * @param tenantDeptList 待转换的租户-部门绑定实体列表（可包含多个实体对象，如分页查询结果、批量查询结果等）
     * @return List<DeptTenantVO> 批量转换后的租户-部门绑定视图列表（用于批量数据展示场景，如列表接口返回）
     */
    List<DeptTenantVO> EnListToVOList(List<DeptTenant> tenantDeptList);
    
}
