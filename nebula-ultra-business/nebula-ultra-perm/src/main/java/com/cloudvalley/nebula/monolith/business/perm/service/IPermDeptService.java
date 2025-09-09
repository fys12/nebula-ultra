package com.cloudvalley.nebula.monolith.business.perm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermDeptRTO;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermDeptVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户部门-租户权限关联 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IPermDeptService extends IService<PermDept> {

    /**
     * 分页查询租户部门-租户权限关联列表
     * @param page 分页参数对象，包含当前页和页大小
     * @return 分页的 PermDeptVO 列表，按创建时间倒序排列
     */
    IPage<PermDeptVO> getPermDeptList(Page<PermDept> page);

    /**
     * 根据多个ID分页批量查询租户部门-租户权限关联
     * @param ids 关联ID列表
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，仅返回未删除的记录
     */
    IPage<PermDeptVO> getPermDeptsByIds(List<Long> ids, Page<PermDept> page);

    /**
     * 根据租户部门ID分页查询其关联的权限列表
     * @param tDeptId 租户部门ID
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，表示该部门拥有的权限
     */
    IPage<PermDeptVO> getPermDeptsByTDeptId(Long tDeptId, Page<PermDept> page);

    /**
     * 根据多个租户部门ID分页批量查询关联权限，并按部门ID分组返回
     * @param tDeptIds 租户部门ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tDeptId，值为对应部门的 PermDeptVO 列表
     */
    IPage<Map<Long, List<PermDeptVO>>> getPermDeptsByTDeptIds(List<Long> tDeptIds, Page<PermDept> page);

    /**
     * 根据租户权限ID分页查询使用该权限的部门列表
     * @param tPermId 租户权限ID
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，表示拥有该权限的部门
     */
    IPage<PermDeptVO> getPermDeptsByTPermId(Long tPermId, Page<PermDept> page);

    /**
     * 根据多个租户权限ID分页批量查询使用部门，并按权限ID分组返回
     * @param tPermIds 租户权限ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tPermId，值为对应权限的部门VO列表
     */
    IPage<Map<Long, List<PermDeptVO>>> getPermDeptsByTPermIds(List<Long> tPermIds, Page<PermDept> page);

    /**
     * 新增租户部门-租户权限关联关系
     * @param permDeptRTO 请求传输对象，包含 tDeptId 和 tPermId
     * @return 成功返回 true，失败返回 false
     */
    boolean createPermDept(PermDeptRTO permDeptRTO);

    /**
     * 更新租户部门-租户权限关联信息
     * @param permDeptRTO 包含 ID、tDeptId、tPermId 的更新对象
     * @return 成功返回 true，失败返回 false
     */
    boolean updatePermDept(PermDeptRTO permDeptRTO);

    /**
     * 更新关联关系状态（启用/禁用该权限在部门中的生效）
     * @param id 关联关系ID
     * @param status 目标状态：true 表示启用，false 表示禁用
     * @return 成功返回 true，失败返回 false
     */
    boolean updatePermDeptStatus(Long id, Boolean status);

    /**
     * 删除租户部门-租户权限关联（物理删除）
     * @param id 关联ID
     * @return 成功返回 true，失败返回 false
     */
    boolean deletePermDept(Long id);

    /**
     * 软删除租户部门-租户权限关联（标记 deleted = true）
     * @param id 关联ID
     * @return 成功返回 true，失败返回 false
     */
    boolean softDeletePermDept(Long id);

    /**
     * 批量删除多个租户部门-租户权限关联（物理删除）
     * @param ids 关联ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    boolean batchDeletePermDepts(List<Long> ids);

    /**
     * 批量软删除多个租户部门-租户权限关联（标记 deleted = true）
     * @param ids 关联ID列表
     * @return 全部操作成功返回 true，否则返回 false
     */
    boolean batchSoftDeletePermDepts(List<Long> ids);

}
