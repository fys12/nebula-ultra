package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupPermConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupPerm;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupPermRTO;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupPermMapper;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupPermService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupPermVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限组-权限关联表 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class GroupPermServiceImpl extends ServiceImpl<GroupPermMapper, GroupPerm> implements IGroupPermService {

    @Autowired
    private GroupPermConverter groupPermConverter;

    /**
     * 分页查询权限组-权限绑定列表
     *
     * @param page 分页参数，包含当前页和每页大小
     * @return 分页的 GroupPermVO 列表
     */
    @Override
    public IPage<GroupPermVO> getGroupPermList(Page<GroupPerm> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<GroupPerm> groupPermPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(groupPermPage.getRecords());

        // 创建新的 IPage<GroupPermVO>，复用原分页参数
        return new Page<GroupPermVO>()
                .setRecords(voList)
                .setTotal(groupPermPage.getTotal())
                .setCurrent(groupPermPage.getCurrent())
                .setSize(groupPermPage.getSize());
    }

    /**
     * 根据ID列表分页查询权限组-权限绑定
     *
     * @param ids 绑定记录ID列表
     * @param page 分页参数
     * @return 分页的 GroupPermVO 列表
     */
    @Override
    public IPage<GroupPermVO> getGroupPermsByIds(List<Long> ids, Page<GroupPerm> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupPerm::getId, ids)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);

        IPage<GroupPerm> groupPermPage = this.page(page, queryWrapper);
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(groupPermPage.getRecords());

        return new Page<GroupPermVO>()
                .setRecords(voList)
                .setTotal(groupPermPage.getTotal())
                .setCurrent(groupPermPage.getCurrent())
                .setSize(groupPermPage.getSize());
    }

    /**
     * 根据系统组ID分页查询其绑定的权限
     *
     * @param groupId 系统组ID
     * @param page 分页参数
     * @return 分页的 GroupPermVO 列表
     */
    @Override
    public IPage<GroupPermVO> getGroupPermsByGroupId(Long groupId, Page<GroupPerm> page) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getSGroupId, groupId)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);

        IPage<GroupPerm> groupPermPage = this.page(page, queryWrapper);
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(groupPermPage.getRecords());

        return new Page<GroupPermVO>()
                .setRecords(voList)
                .setTotal(groupPermPage.getTotal())
                .setCurrent(groupPermPage.getCurrent())
                .setSize(groupPermPage.getSize());
    }

    /**
     * 根据多个系统组ID分页查询权限绑定关系，并按组ID分组返回
     *
     * @param groupIds 系统组ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 groupId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupPermVO>>> getGroupPermsByGroupIds(List<Long> groupIds, Page<GroupPerm> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                groupIds,
                // 分页参数
                page,
                // 查询条件：按系统组ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(GroupPerm::getSGroupId, groupIds)
                        .eq(GroupPerm::getDeleted, false)
                        .orderByDesc(GroupPerm::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                groupPermConverter::EnListToVOList,
                // 按系统组ID分组
                GroupPermVO::getSGroupId
        );
    }

    /**
     * 根据租户权限ID分页查询绑定的系统组
     *
     * @param permId 租户权限ID
     * @param page 分页参数
     * @return 分页的 GroupPermVO 列表
     */
    @Override
    public IPage<GroupPermVO> getGroupPermsByPermId(Long permId, Page<GroupPerm> page) {
        LambdaQueryWrapper<GroupPerm> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupPerm::getTPermId, permId)
                .eq(GroupPerm::getDeleted, false)
                .orderByDesc(GroupPerm::getCreatedAt);

        IPage<GroupPerm> groupPermPage = this.page(page, queryWrapper);
        List<GroupPermVO> voList = groupPermConverter.EnListToVOList(groupPermPage.getRecords());

        return new Page<GroupPermVO>()
                .setRecords(voList)
                .setTotal(groupPermPage.getTotal())
                .setCurrent(groupPermPage.getCurrent())
                .setSize(groupPermPage.getSize());
    }

    /**
     * 根据多个租户权限ID分页查询绑定关系，并按权限ID分组返回
     *
     * @param permIds 租户权限ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 permId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupPermVO>>> getGroupPermsByPermIds(List<Long> permIds, Page<GroupPerm> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                permIds,
                // 分页参数
                page,
                // 查询条件：按权限ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(GroupPerm::getTPermId, permIds)
                        .eq(GroupPerm::getDeleted, false)
                        .orderByDesc(GroupPerm::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                groupPermConverter::EnListToVOList,
                // 按权限ID分组
                GroupPermVO::getTPermId
        );
    }

    /**
     * 新增权限组-权限绑定关系
     *
     * @param groupPermRTO 请求传输对象，包含绑定信息
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroupPerm(GroupPermRTO groupPermRTO) {
        GroupPerm groupPerm = new GroupPerm();
        BeanUtils.copyProperties(groupPermRTO, groupPerm);

        // 设置默认值
        groupPerm.setId(GeneratorUtils.generateId());
        groupPerm.setCreatedAt(LocalDateTime.now());
        groupPerm.setUpdatedAt(LocalDateTime.now());
        if (groupPermRTO.getDeleted() == null) {
            groupPerm.setDeleted(false);
        }

        return this.save(groupPerm);
    }

    /**
     * 更新权限组-权限绑定关系
     *
     * @param groupPermRTO 请求传输对象，包含更新信息，ID不能为空
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroupPerm(GroupPermRTO groupPermRTO) {
        if (groupPermRTO.getId() == null) {
            return false;
        }

        GroupPerm groupPerm = new GroupPerm();
        BeanUtils.copyProperties(groupPermRTO, groupPerm);
        groupPerm.setUpdatedAt(LocalDateTime.now());

        LambdaUpdateWrapper<GroupPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupPerm::getId, groupPermRTO.getId())
                .eq(GroupPerm::getDeleted, false);

        return this.update(groupPerm, updateWrapper);
    }

    /**
     * 删除权限组-权限绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupPerm(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除权限组-权限绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteGroupPerm(Long id) {
        LambdaUpdateWrapper<GroupPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupPerm::getId, id)
                .eq(GroupPerm::getDeleted, false)
                .set(GroupPerm::getDeleted, true)
                .set(GroupPerm::getUpdatedAt, LocalDateTime.now());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除权限组-权限绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteGroupPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除权限组-权限绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return 全部软删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteGroupPerms(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<GroupPerm> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(GroupPerm::getId, ids)
                .eq(GroupPerm::getDeleted, false)
                .set(GroupPerm::getDeleted, true)
                .set(GroupPerm::getUpdatedAt, LocalDateTime.now());

        return this.update(updateWrapper);
    }

}
