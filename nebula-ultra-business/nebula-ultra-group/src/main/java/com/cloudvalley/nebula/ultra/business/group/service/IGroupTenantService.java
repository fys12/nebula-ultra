package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupTenantRTO;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户绑定 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IGroupTenantService extends IService<GroupTenant> {

    /**
     * 分页查询租户-组绑定列表
     *
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    IPage<GroupTenantVO> getGroupTenantList(Page<GroupTenant> page);

    /**
     * 根据ID列表分页查询租户-组绑定
     *
     * @param ids 绑定记录ID列表
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    IPage<GroupTenantVO> getGroupTenantsByIds(List<Long> ids, Page<GroupTenant> page);

    /**
     * 根据系统租户ID分页查询其绑定的系统组
     *
     * @param sTenantId 系统租户ID
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    IPage<GroupTenantVO> getGroupTenantsBySTenantId(Long sTenantId, Page<GroupTenant> page);

    /**
     * 根据多个系统租户ID分页查询绑定关系，并按租户ID分组返回
     *
     * @param sTenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 sTenantId，值为对应 VO 列表
     */
    IPage<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySTenantIds(List<Long> sTenantIds, Page<GroupTenant> page);

    /**
     * 根据系统组ID分页查询绑定该组的系统租户
     *
     * @param sGroupId 系统组ID
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    IPage<GroupTenantVO> getGroupTenantsBySGroupId(Long sGroupId, Page<GroupTenant> page);

    /**
     * 根据多个系统组ID分页查询绑定关系，并按组ID分组返回
     *
     * @param sGroupIds 系统组ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 sGroupId，值为对应 VO 列表
     */
    IPage<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySGroupIds(List<Long> sGroupIds, Page<GroupTenant> page);

    /**
     * 新增租户-组绑定关系
     *
     * @param groupTenantRTO 请求传输对象，包含绑定信息
     * @return 成功返回 true，否则返回 false
     */
    boolean createGroupTenant(GroupTenantRTO groupTenantRTO);

    /**
     * 更新租户-组绑定关系
     *
     * @param groupTenantRTO 请求传输对象，包含更新信息，ID不能为空
     * @return 成功返回 true，否则返回 false
     */
    boolean updateGroupTenant(GroupTenantRTO groupTenantRTO);

    /**
     * 删除租户-组绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    boolean deleteGroupTenant(Long id);

    /**
     * 软删除租户-组绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    boolean softDeleteGroupTenant(Long id);

    /**
     * 批量删除租户-组绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    boolean batchDeleteGroupTenants(List<Long> ids);

    /**
     * 批量软删除租户-组绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return 全部软删除成功返回 true，否则返回 false
     */
    boolean batchSoftDeleteGroupTenants(List<Long> ids);
    
}
