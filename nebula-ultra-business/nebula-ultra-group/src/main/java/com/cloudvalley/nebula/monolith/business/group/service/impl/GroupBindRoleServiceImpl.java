package com.cloudvalley.nebula.monolith.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.group.converter.GroupBindRoleConverter;
import com.cloudvalley.nebula.monolith.business.group.model.entity.GroupBindRole;
import com.cloudvalley.nebula.monolith.business.group.model.rto.GroupBindRoleRTO;
import com.cloudvalley.nebula.monolith.business.group.mapper.GroupBindRoleMapper;
import com.cloudvalley.nebula.monolith.business.group.service.IGroupBindRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.group.model.vo.GroupBindRoleVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户角色绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class GroupBindRoleServiceImpl extends ServiceImpl<GroupBindRoleMapper, GroupBindRole> implements IGroupBindRoleService {

    @Autowired
    private GroupBindRoleConverter groupBindRoleConverter;

    /**
     * 分页查询组-租户角色绑定列表
     *
     * @param page 分页参数，包含当前页和每页大小
     * @return 分页的 GroupBindRoleVO 列表
     */
    @Override
    public IPage<GroupBindRoleVO> getGroupBindRoleList(Page<GroupBindRole> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<GroupBindRole> groupBindRolePage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(groupBindRolePage.getRecords());

        // 创建新的 IPage<GroupBindRoleVO>，复用原分页参数
        return new Page<GroupBindRoleVO>()
                .setRecords(voList)
                .setTotal(groupBindRolePage.getTotal())
                .setCurrent(groupBindRolePage.getCurrent())
                .setSize(groupBindRolePage.getSize());
    }

    /**
     * 根据ID列表分页查询组-租户角色绑定
     *
     * @param ids 绑定记录ID列表
     * @param page 分页参数
     * @return 分页的 GroupBindRoleVO 列表
     */
    @Override
    public IPage<GroupBindRoleVO> getGroupBindRolesByIds(List<Long> ids, Page<GroupBindRole> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindRole::getId, ids)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);

        IPage<GroupBindRole> groupBindRolePage = this.page(page, queryWrapper);
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(groupBindRolePage.getRecords());

        return new Page<GroupBindRoleVO>()
                .setRecords(voList)
                .setTotal(groupBindRolePage.getTotal())
                .setCurrent(groupBindRolePage.getCurrent())
                .setSize(groupBindRolePage.getSize());
    }

    /**
     * 根据系统组ID分页查询其绑定的租户角色
     *
     * @param groupId 系统组ID
     * @param page 分页参数
     * @return 分页的 GroupBindRoleVO 列表
     */
    @Override
    public IPage<GroupBindRoleVO> getGroupBindRolesByGroupId(Long groupId, Page<GroupBindRole> page) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getSGroupId, groupId)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);

        IPage<GroupBindRole> groupBindRolePage = this.page(page, queryWrapper);
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(groupBindRolePage.getRecords());

        return new Page<GroupBindRoleVO>()
                .setRecords(voList)
                .setTotal(groupBindRolePage.getTotal())
                .setCurrent(groupBindRolePage.getCurrent())
                .setSize(groupBindRolePage.getSize());
    }

    /**
     * 根据多个系统组ID分页查询绑定关系，并按组ID分组返回
     *
     * @param groupIds 系统组ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 groupId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByGroupIds(List<Long> groupIds, Page<GroupBindRole> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                groupIds,
                // 分页参数
                page,
                // 查询条件：按组ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(GroupBindRole::getSGroupId, groupIds)
                        .eq(GroupBindRole::getDeleted, false)
                        .orderByDesc(GroupBindRole::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                groupBindRoleConverter::EnListToVOList,
                // 按组ID分组
                GroupBindRoleVO::getSGroupId
        );
    }

    /**
     * 根据租户角色ID分页查询绑定的系统组
     *
     * @param roleId 租户角色ID
     * @param page 分页参数
     * @return 分页的 GroupBindRoleVO 列表
     */
    @Override
    public IPage<GroupBindRoleVO> getGroupBindRolesByRoleId(Long roleId, Page<GroupBindRole> page) {
        LambdaQueryWrapper<GroupBindRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindRole::getTRoleId, roleId)
                .eq(GroupBindRole::getDeleted, false)
                .orderByDesc(GroupBindRole::getCreatedAt);

        IPage<GroupBindRole> groupBindRolePage = this.page(page, queryWrapper);
        List<GroupBindRoleVO> voList = groupBindRoleConverter.EnListToVOList(groupBindRolePage.getRecords());

        return new Page<GroupBindRoleVO>()
                .setRecords(voList)
                .setTotal(groupBindRolePage.getTotal())
                .setCurrent(groupBindRolePage.getCurrent())
                .setSize(groupBindRolePage.getSize());
    }

    /**
     * 根据多个租户角色ID分页查询绑定关系，并按角色ID分组返回
     *
     * @param roleIds 租户角色ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 roleId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupBindRoleVO>>> getGroupBindRolesByRoleIds(List<Long> roleIds, Page<GroupBindRole> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                roleIds,
                // 分页参数
                page,
                // 查询条件：按角色ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(GroupBindRole::getTRoleId, roleIds)
                        .eq(GroupBindRole::getDeleted, false)
                        .orderByDesc(GroupBindRole::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                groupBindRoleConverter::EnListToVOList,
                // 按角色ID分组
                GroupBindRoleVO::getTRoleId
        );
    }

    /**
     * 新增组-租户角色绑定
     *
     * @param groupBindRoleRTO 请求传输对象，包含绑定信息
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroupBindRole(GroupBindRoleRTO groupBindRoleRTO) {
        GroupBindRole groupBindRole = new GroupBindRole();
        BeanUtils.copyProperties(groupBindRoleRTO, groupBindRole);

        // 设置默认值
        groupBindRole.setId(GeneratorUtils.generateId());
        groupBindRole.setCreatedAt(GeneratorUtils.generateCurrentTime());
        groupBindRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (groupBindRole.getDeleted() == null) {
            groupBindRole.setDeleted(false);
        }

        return this.save(groupBindRole);
    }

    /**
     * 更新组-租户角色绑定
     *
     * @param groupBindRoleRTO 请求传输对象，包含更新信息，ID不能为空
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroupBindRole(GroupBindRoleRTO groupBindRoleRTO) {
        if (groupBindRoleRTO.getId() == null) {
            return false;
        }

        GroupBindRole groupBindRole = new GroupBindRole();
        BeanUtils.copyProperties(groupBindRoleRTO, groupBindRole);
        groupBindRole.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<GroupBindRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupBindRole::getId, groupBindRoleRTO.getId())
                .eq(GroupBindRole::getDeleted, false);

        return this.update(groupBindRole, updateWrapper);
    }

    /**
     * 删除组-租户角色绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupBindRole(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除组-租户角色绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteGroupBindRole(Long id) {
        LambdaUpdateWrapper<GroupBindRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupBindRole::getId, id)
                .eq(GroupBindRole::getDeleted, false)
                .set(GroupBindRole::getDeleted, true)
                .set(GroupBindRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除组-租户角色绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteGroupBindRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除组-租户角色绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return 全部软删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteGroupBindRoles(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<GroupBindRole> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(GroupBindRole::getId, ids)
                .eq(GroupBindRole::getDeleted, false)
                .set(GroupBindRole::getDeleted, true)
                .set(GroupBindRole::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }
    
}
