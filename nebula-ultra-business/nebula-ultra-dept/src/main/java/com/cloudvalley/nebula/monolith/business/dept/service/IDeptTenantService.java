package com.cloudvalley.nebula.monolith.business.dept.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.DeptTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.DeptTenantRTO;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.DeptTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-部门绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IDeptTenantService extends IService<DeptTenant> {

    /**
     * 查询租户 - 部门绑定列表 [分页]
     * @param page 分页参数
     * @return 租户 - 部门绑定列表
     */
    IPage<DeptTenantVO> getDeptTenantList(Page<DeptTenant> page);

    /**
     * 根据租户 - 部门绑定 id 批量查询绑定关系 [分页]
     * @param ids 绑定ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptTenantVO> getDeptTenantsByIds(List<Long> ids, Page<DeptTenant> page);

    /**
     * 根据系统租户 id 查询绑定关系 [分页]
     * @param sTenantId 系统租户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptTenantVO> getDeptTenantsBySTenantId(Long sTenantId, Page<DeptTenant> page);

    /**
     * 根据系统租户 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 分组分页结果（records 中仅包含一个 Map）
     */
    IPage<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySTenantIds(List<Long> sTenantIds, Page<DeptTenant> page);

    /**
     * 根据系统部门 id 查询绑定关系 [分页]
     * @param sDeptId 系统部门ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<DeptTenantVO> getDeptTenantsBySDeptId(Long sDeptId, Page<DeptTenant> page);

    /**
     * 根据系统部门 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sDeptIds 系统部门ID列表
     * @param page 分页参数
     * @return 分组分页结果（records 中仅包含一个 Map）
     */
    IPage<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySDeptIds(List<Long> sDeptIds, Page<DeptTenant> page);

    /**
     * 新增租户 - 部门绑定
     * @param tenantDeptRTO 绑定信息
     * @return 操作结果
     */
    boolean createDeptTenant(DeptTenantRTO tenantDeptRTO);

    /**
     * 更新租户 - 部门绑定
     * @param tenantDeptRTO 绑定信息
     * @return 操作结果
     */
    boolean updateDeptTenant(DeptTenantRTO tenantDeptRTO);

    /**
     * 更新绑定状态（该部门在该租户中的启用状态）
     * @param id 绑定ID
     * @param state 状态
     * @return 操作结果
     */
    boolean updateDeptTenantState(Long id, Boolean state);

    /**
     * 删除租户 - 部门绑定（真删）
     * @param id 绑定ID
     * @return 操作结果
     */
    boolean deleteDeptTenant(Long id);

    /**
     * 软删除租户 - 部门绑定
     * @param id 绑定ID
     * @return 操作结果
     */
    boolean softDeleteDeptTenant(Long id);

    /**
     * 批量删除租户 - 部门绑定（真删）
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    boolean batchDeleteDeptTenants(List<Long> ids);

    /**
     * 批量软删除租户 - 部门绑定
     * @param ids 绑定ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteDeptTenants(List<Long> ids);
    
}
