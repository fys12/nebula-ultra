package com.cloudvalley.nebula.ultra.shared.api.dept.service;


import com.cloudvalley.nebula.ultra.shared.api.dept.model.vo.DeptTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IDeptTenantCommonService {

    /**
     * 根据租户 - 部门绑定 id 查询绑定关系（单个）
     * @param id 绑定ID
     * @return 绑定信息
     */
    DeptTenantVO getDeptTenantById(Long id);


    /**
     * 根据租户 - 部门绑定 id 批量查询绑定关系 [全量]
     * @param ids 绑定ID列表
     * @return 绑定信息列表
     */
    List<DeptTenantVO> getDeptTenantsByIds(List<Long> ids);

    /**
     * 根据系统租户 id 查询绑定关系 [全量]
     * @param sTenantId 系统租户ID
     * @return 绑定信息列表
     */
    List<DeptTenantVO> getDeptTenantsBySTenantId(Long sTenantId);

    /**
     * 根据系统租户 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sTenantIds 系统租户ID列表
     * @return 按系统租户ID分组的绑定关系映射列表
     */
    List<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySTenantIds(List<Long> sTenantIds);

    /**
     * 根据系统部门 id 查询绑定关系 [全量]
     * @param sDeptId 系统部门ID
     * @return 绑定信息列表
     */
    List<DeptTenantVO> getDeptTenantsBySDeptId(Long sDeptId);

    /**
     * 根据系统部门 id 批量查询绑定关系 [全量] - 批量返回分组结果
     * @param sDeptIds 系统部门ID列表
     * @return 按系统部门ID分组的绑定关系映射列表
     */
    List<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySDeptIds(List<Long> sDeptIds);

    /**
     * 根据系统租户 ID 查询系统部门 ID 列表
     * @param sTenantId 系统租户ID
     * @return 系统部门ID集合
     */
    Set<Long> getSDeptIdsBySTenantId(Long sTenantId);

    /**
     * 根据系统部门 ID 查询系统租户 ID 列表
     * @param sDeptId 系统部门ID
     * @return 系统租户ID集合
     */
    Set<Long> getSTenantIdsBySDeptId(Long sDeptId);
}
