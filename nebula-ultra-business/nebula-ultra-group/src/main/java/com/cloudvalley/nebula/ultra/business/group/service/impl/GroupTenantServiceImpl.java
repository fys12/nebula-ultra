package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupTenantConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupTenant;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupTenantRTO;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupTenantMapper;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class GroupTenantServiceImpl extends ServiceImpl<GroupTenantMapper, GroupTenant> implements IGroupTenantService {

    @Autowired
    private GroupTenantConverter groupTenantConverter;

    /**
     * 分页查询租户-组绑定列表
     *
     * @param page 分页参数，包含当前页和每页大小
     * @return 分页的 GroupTenantVO 列表
     */
    @Override
    public IPage<GroupTenantVO> getGroupTenantList(Page<GroupTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<GroupTenant> groupTenantPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(groupTenantPage.getRecords());

        // 创建新的 IPage<GroupTenantVO>，复用原分页参数
        return new Page<GroupTenantVO>()
                .setRecords(voList)
                .setTotal(groupTenantPage.getTotal())
                .setCurrent(groupTenantPage.getCurrent())
                .setSize(groupTenantPage.getSize());
    }

    /**
     * 根据ID列表分页查询租户-组绑定
     *
     * @param ids 绑定记录ID列表
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    @Override
    public IPage<GroupTenantVO> getGroupTenantsByIds(List<Long> ids, Page<GroupTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupTenant::getId, ids)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);

        IPage<GroupTenant> groupTenantPage = this.page(page, queryWrapper);
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(groupTenantPage.getRecords());

        return new Page<GroupTenantVO>()
                .setRecords(voList)
                .setTotal(groupTenantPage.getTotal())
                .setCurrent(groupTenantPage.getCurrent())
                .setSize(groupTenantPage.getSize());
    }

    /**
     * 根据系统租户ID分页查询其绑定的系统组
     *
     * @param sTenantId 系统租户ID
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    @Override
    public IPage<GroupTenantVO> getGroupTenantsBySTenantId(Long sTenantId, Page<GroupTenant> page) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSTenantId, sTenantId)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);

        IPage<GroupTenant> groupTenantPage = this.page(page, queryWrapper);
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(groupTenantPage.getRecords());

        return new Page<GroupTenantVO>()
                .setRecords(voList)
                .setTotal(groupTenantPage.getTotal())
                .setCurrent(groupTenantPage.getCurrent())
                .setSize(groupTenantPage.getSize());
    }

    /**
     * 根据多个系统租户ID分页查询绑定关系，并按租户ID分组返回
     *
     * @param sTenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 sTenantId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySTenantIds(List<Long> sTenantIds, Page<GroupTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                sTenantIds,
                // 分页参数
                page,
                // 查询条件：按系统租户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(GroupTenant::getSTenantId, sTenantIds)
                        .eq(GroupTenant::getDeleted, false)
                        .orderByDesc(GroupTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                groupTenantConverter::EnListToVOList,
                // 按系统租户ID分组
                GroupTenantVO::getSTenantId
        );
    }

    /**
     * 根据系统组ID分页查询绑定该组的系统租户
     *
     * @param sGroupId 系统组ID
     * @param page 分页参数
     * @return 分页的 GroupTenantVO 列表
     */
    @Override
    public IPage<GroupTenantVO> getGroupTenantsBySGroupId(Long sGroupId, Page<GroupTenant> page) {
        LambdaQueryWrapper<GroupTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupTenant::getSGroupId, sGroupId)
                .eq(GroupTenant::getDeleted, false)
                .orderByDesc(GroupTenant::getCreatedAt);

        IPage<GroupTenant> groupTenantPage = this.page(page, queryWrapper);
        List<GroupTenantVO> voList = groupTenantConverter.EnListToVOList(groupTenantPage.getRecords());

        return new Page<GroupTenantVO>()
                .setRecords(voList)
                .setTotal(groupTenantPage.getTotal())
                .setCurrent(groupTenantPage.getCurrent())
                .setSize(groupTenantPage.getSize());
    }

    /**
     * 根据多个系统组ID分页查询绑定关系，并按组ID分组返回
     *
     * @param sGroupIds 系统组ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 sGroupId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupTenantVO>>> getGroupTenantsBySGroupIds(List<Long> sGroupIds, Page<GroupTenant> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                sGroupIds,
                // 分页参数
                page,
                // 查询条件
                wrapper -> wrapper.in(GroupTenant::getSGroupId, sGroupIds)
                        .eq(GroupTenant::getDeleted, false)
                        .orderByDesc(GroupTenant::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体转 VO 列表
                groupTenantConverter::EnListToVOList,
                // 按系统组ID分组
                GroupTenantVO::getSGroupId
        );
    }

    /**
     * 新增租户-组绑定关系
     *
     * @param groupTenantRTO 请求传输对象，包含绑定信息
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroupTenant(GroupTenantRTO groupTenantRTO) {
        GroupTenant groupTenant = new GroupTenant();
        BeanUtils.copyProperties(groupTenantRTO, groupTenant);

        // 设置默认值
        groupTenant.setId(GeneratorUtils.generateId());
        groupTenant.setCreatedAt(GeneratorUtils.generateCurrentTime());
        groupTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        groupTenant.setCreatedById(FetchUtils.getCurrentUserId());
        if (groupTenant.getDeleted() == null) {
            groupTenant.setDeleted(false);
        }

        return this.save(groupTenant);
    }

    /**
     * 更新租户-组绑定关系
     *
     * @param groupTenantRTO 请求传输对象，包含更新信息，ID不能为空
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroupTenant(GroupTenantRTO groupTenantRTO) {
        if (groupTenantRTO.getId() == null) {
            return false;
        }

        GroupTenant groupTenant = new GroupTenant();
        BeanUtils.copyProperties(groupTenantRTO, groupTenant);
        groupTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<GroupTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupTenant::getId, groupTenantRTO.getId())
                .eq(GroupTenant::getDeleted, false);

        return this.update(groupTenant, updateWrapper);
    }

    /**
     * 删除租户-组绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户-组绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteGroupTenant(Long id) {
        LambdaUpdateWrapper<GroupTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupTenant::getId, id)
                .eq(GroupTenant::getDeleted, false)
                .set(GroupTenant::getDeleted, true)
                .set(GroupTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除租户-组绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteGroupTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除租户-组绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return 全部软删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteGroupTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<GroupTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(GroupTenant::getId, ids)
                .eq(GroupTenant::getDeleted, false)
                .set(GroupTenant::getDeleted, true)
                .set(GroupTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }
    
}
