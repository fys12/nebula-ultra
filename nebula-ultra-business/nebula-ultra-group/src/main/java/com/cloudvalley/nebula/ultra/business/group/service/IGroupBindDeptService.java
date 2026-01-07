package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindDept;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupBindDeptRTO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户部门绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IGroupBindDeptService extends IService<GroupBindDept> {

    /**
     * 查询组-租户部门绑定列表 [分页]
     * @param page 分页参数
     * @return 组-租户部门绑定列表
     */
    IPage<GroupBindDeptVO> getGroupBindDeptList(Page<GroupBindDept> page);

    /**
     * 根据ID批量查询绑定关系 [分页]
     * @param ids 主键ID列表
     * @param page 分页参数
     * @return 绑定信息分页列表
     */
    IPage<GroupBindDeptVO> getGroupBindDeptsByIds(List<Long> ids, Page<GroupBindDept> page);

    /**
     * 根据租户组ID查询绑定关系 [分页]
     * @param tGroupId 租户组ID
     * @param page 分页参数
     * @return 绑定列表
     */
    IPage<GroupBindDeptVO> getGroupBindDeptsBySGroupId(Long tGroupId, Page<GroupBindDept> page);

    /**
     * 根据租户组ID批量查询绑定关系 [分页] - 返回分组结果
     * @param tGroupIds 租户组ID列表
     * @param page 分页参数
     * @return 按系统组ID分组的绑定分页列表
     */
    IPage<Map<Long, List<GroupBindDeptVO>>> getGroupBindDeptsBySGroupIds(List<Long> tGroupIds, Page<GroupBindDept> page);

    /**
     * 根据租户部门ID查询绑定关系 [分页]
     * @param tDeptId 租户部门ID
     * @param page 分页参数
     * @return 绑定列表
     */
    IPage<GroupBindDeptVO> getGroupBindDeptsByTDeptId(Long tDeptId, Page<GroupBindDept> page);

    /**
     * 根据租户部门ID批量查询绑定关系 [分页] - 返回分组结果
     * @param tDeptIds 租户部门ID列表
     * @param page 分页参数
     * @return 按租户部门ID分组的绑定分页列表
     */
    IPage<Map<Long, List<GroupBindDeptVO>>> getGroupBindDeptsByTDeptIds(List<Long> tDeptIds, Page<GroupBindDept> page);

    /**
     * 新增组-租户部门绑定
     * @param groupDeptRTO 绑定信息
     * @return 操作结果
     */
    boolean createGroupBindDept(GroupBindDeptRTO groupDeptRTO);

    /**
     * 更新组-租户部门绑定
     * @param groupDeptRTO 绑定信息
     * @return 操作结果
     */
    boolean updateGroupBindDept(GroupBindDeptRTO groupDeptRTO);

    /**
     * 删除组-租户部门绑定（真删）
     * @param id 主键ID
     * @return 操作结果
     */
    boolean deleteGroupBindDept(Long id);

    /**
     * 软删除组-租户部门绑定
     * @param id 主键ID
     * @return 操作结果
     */
    boolean softDeleteGroupBindDept(Long id);

    /**
     * 批量删除组-租户部门绑定（真删）
     * @param ids 主键ID列表
     * @return 操作结果
     */
    boolean batchDeleteGroupBindDepts(List<Long> ids);

    /**
     * 批量软删除组-租户部门绑定
     * @param ids 主键ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteGroupBindDepts(List<Long> ids);
    
}
