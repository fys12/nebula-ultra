package com.cloudvalley.nebula.ultra.business.role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.role.model.entity.RoleTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.role.model.rto.RoleTenantRTO;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.RoleTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-角色绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IRoleTenantService extends IService<RoleTenant> {

    /**
     * 查询租户角色列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleTenantVO> getRoleTenantList(Page<RoleTenant> page);

    /**
     * 根据租户角色id查询租户角色(可查询单个或多个，传入的id可以单个或多个) [通过租户角色id获取一个或多个「租户-角色」绑定关系的完整信息][分页]
     * @param ids 租户角色ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleTenantVO> getRoleTenantsByIds(List<Long> ids, Page<RoleTenant> page);

    /**
     * 根据系统租户id查询租户角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）租户有哪些角色][分页]
     * @param tenantId 系统租户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleTenantVO> getRoleTenantsByTenantId(Long tenantId, Page<RoleTenant> page);

    /**
     * 根据系统租户id查询租户角色(批量查询多个租户) [查看某个（或多个）租户有哪些角色][分页]
     * @param tenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 分页结果，按租户ID分组
     */
    IPage<Map<Long, List<RoleTenantVO>>> getRoleTenantsByTenantIds(List<Long> tenantIds, Page<RoleTenant> page);

    /**
     * 根据系统角色id查询租户角色(可查询单个或多个，传入的id可以单个或多个) [查看某个（或多个）角色被哪些租户绑定][分页]
     * @param roleId 系统角色ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<RoleTenantVO> getRoleTenantsByRoleId(Long roleId, Page<RoleTenant> page);

    /**
     * 根据系统角色id查询租户角色(批量查询多个角色) [查看某个（或多个）角色被哪些租户绑定][分页]
     * @param roleIds 系统角色ID列表
     * @param page 分页参数
     * @return 分页结果，按角色ID分组
     */
    IPage<Map<Long, List<RoleTenantVO>>> getRoleTenantsByRoleIds(List<Long> roleIds, Page<RoleTenant> page);

    /**
     * 新增租户角色
     * @param roleTenantRTO 租户角色信息
     * @return 是否成功
     */
    boolean createRoleTenant(RoleTenantRTO roleTenantRTO);

    /**
     * 更新租户角色
     * @param roleTenantRTO 租户角色信息
     * @return 是否成功
     */
    boolean updateRoleTenant(RoleTenantRTO roleTenantRTO);

    /**
     * 更新租户角色状态 [该角色在该租户中的状态]
     * @param id 租户角色ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateRoleTenantState(Long id, Boolean state);

    /**
     * 删除租户角色(真)
     * @param id 租户角色ID
     * @return 是否成功
     */
    boolean deleteRoleTenant(Long id);

    /**
     * 软删除租户角色
     * @param id 租户角色ID
     * @return 是否成功
     */
    boolean softDeleteRoleTenant(Long id);

    /**
     * 批量删除租户角色(真)
     * @param ids 租户角色ID列表
     * @return 是否成功
     */
    boolean batchDeleteRoleTenants(List<Long> ids);

    /**
     * 批量软删除租户角色
     * @param ids 租户角色ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteRoleTenants(List<Long> ids);
    
}
