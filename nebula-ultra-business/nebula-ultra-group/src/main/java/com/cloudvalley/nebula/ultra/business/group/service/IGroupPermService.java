package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupPerm;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupPermRTO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupPermVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限组-权限关联表 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IGroupPermService extends IService<GroupPerm> {

    /**
     * 查询权限组-权限关联列表 [分页]
     * @param page 分页参数
     * @return 权限组关联列表
     */
    IPage<GroupPermVO> getGroupPermList(Page<GroupPerm> page);

    /**
     * 根据ID批量查询权限组关联 [分页]
     * @param ids 关联ID列表
     * @param page 分页参数
     * @return 权限组关联信息分页列表
     */
    IPage<GroupPermVO> getGroupPermsByIds(List<Long> ids, Page<GroupPerm> page);

    /**
     * 根据租户组ID查询权限组关联 [分页]
     * @param tGroupId 租户组ID
     * @param page 分页参数
     * @return 权限组关联列表
     */
    IPage<GroupPermVO> getGroupPermsByGroupId(Long tGroupId, Page<GroupPerm> page);

    /**
     * 根据租户组ID批量查询权限组关联 [分页] - 返回分组结果
     * @param tGroupIds 租户组ID列表
     * @param page 分页参数
     * @return 按系统组ID分组的权限关联分页列表
     */
    IPage<Map<Long, List<GroupPermVO>>> getGroupPermsByGroupIds(List<Long> tGroupIds, Page<GroupPerm> page);

    /**
     * 根据租户权限ID查询权限组关联 [分页]
     * @param permId 租户权限ID
     * @param page 分页参数
     * @return 权限组关联列表
     */
    IPage<GroupPermVO> getGroupPermsByPermId(Long permId, Page<GroupPerm> page);

    /**
     * 根据租户权限ID批量查询权限组关联 [分页] - 返回分组结果
     * @param permIds 租户权限ID列表
     * @param page 分页参数
     * @return 按租户权限ID分组的权限关联分页列表
     */
    IPage<Map<Long, List<GroupPermVO>>> getGroupPermsByPermIds(List<Long> permIds, Page<GroupPerm> page);

    /**
     * 新增权限组权限关联
     * @param groupPermRTO 权限组关联信息
     * @return 操作结果
     */
    boolean createGroupPerm(GroupPermRTO groupPermRTO);

    /**
     * 更新权限组权限关联
     * @param groupPermRTO 权限组关联信息
     * @return 操作结果
     */
    boolean updateGroupPerm(GroupPermRTO groupPermRTO);

    /**
     * 删除权限组关联（真删除）
     * @param id 关联ID
     * @return 操作结果
     */
    boolean deleteGroupPerm(Long id);

    /**
     * 软删除权限组关联
     * @param id 关联ID
     * @return 操作结果
     */
    boolean softDeleteGroupPerm(Long id);

    /**
     * 批量删除权限组关联（真删除）
     * @param ids 关联ID列表
     * @return 操作结果
     */
    boolean batchDeleteGroupPerms(List<Long> ids);

    /**
     * 批量软删除权限组关联
     * @param ids 关联ID列表
     * @return 操作结果
     */
    boolean batchSoftDeleteGroupPerms(List<Long> ids);
    
}
