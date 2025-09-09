package com.cloudvalley.nebula.monolith.business.user.converter;

import com.cloudvalley.nebula.monolith.business.user.model.entity.UserTenant;
import com.cloudvalley.nebula.monolith.shared.api.user.model.vo.UserTenantVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserTenantConverter {

    /**
     * 将租户用户实体类（UserTenant）转换为租户用户视图对象（UserTenantVO）
     * 核心映射规则：处理实体与VO的字段命名差异，将实体中大写开头的关联字段映射到VO中小写开头的对应字段，
     * 即实体的STenantId（系统租户主键ID）映射到VO的sTenantId，实体的SUserId（系统用户主键ID）映射到VO的sUserId；
     * 其他字段名一致的属性（如id、username、state、deleted、createdAt等）将自动映射，无需额外配置
     * @param tenantUser 待转换的租户用户实体对象（包含租户用户的完整业务数据，如租户关联ID、用户基础信息、状态、创建时间等）
     * @return UserTenantVO 转换后的租户用户视图对象（用于接口返回，屏蔽实体层敏感/冗余字段，适配前端展示或下游服务数据交互需求）
     */
    @Mapping(source = "STenantId", target = "sTenantId")
    @Mapping(source = "SUserId", target = "sUserId")
    UserTenantVO EnToVO(UserTenant tenantUser);

    /**
     * 将租户用户实体列表（List<UserTenant>）批量转换为租户用户视图对象列表（List<UserTenantVO>）
     * 内部通过循环调用单个实体转换方法（EnToVO）实现批量处理，确保每个实体与对应VO的映射规则统一，
     * 同时保持原列表的数据顺序和元素数量一致性，避免批量转换时出现数据错位
     * @param tenantUserList 待转换的租户用户实体列表（可来自分页查询、批量查询等场景，包含多个租户用户的实体数据）
     * @return List<UserTenantVO> 批量转换后的租户用户视图列表（适用于租户用户列表展示、批量数据导出等场景）
     */
    List<UserTenantVO> EnListToVOList(List<UserTenant> tenantUserList);

}
