package com.cloudvalley.nebula.ultra.business.group.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.group.converter.GroupBindDeptConverter;
import com.cloudvalley.nebula.ultra.business.group.model.entity.GroupBindDept;
import com.cloudvalley.nebula.ultra.business.group.model.rto.GroupBindDeptRTO;
import com.cloudvalley.nebula.ultra.business.group.mapper.GroupBindDeptMapper;
import com.cloudvalley.nebula.ultra.business.group.service.IGroupBindDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.group.model.vo.GroupBindDeptVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组-租户部门绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class GroupBindDeptServiceImpl extends ServiceImpl<GroupBindDeptMapper, GroupBindDept> implements IGroupBindDeptService {

    @Autowired
    private GroupBindDeptConverter groupBindDeptConverter;

    /**
     * 分页查询组-租户部门绑定列表
     *
     * @param page 分页参数，包含当前页和每页大小
     * @return 分页的 GroupBindDeptVO 列表
     */
    @Override
    public IPage<GroupBindDeptVO> getGroupBindDeptList(Page<GroupBindDept> page) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        IPage<GroupBindDept> dataPage = this.page(page, queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<GroupBindDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据ID列表分页查询组-租户部门绑定
     *
     * @param ids 绑定记录ID列表
     * @param page 分页参数
     * @return 分页的 GroupBindDeptVO 列表
     */
    @Override
    public IPage<GroupBindDeptVO> getGroupBindDeptsByIds(List<Long> ids, Page<GroupBindDept> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(GroupBindDept::getId, ids)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        IPage<GroupBindDept> dataPage = this.page(page, queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<GroupBindDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据租户组ID分页查询其绑定的租户部门
     *
     * @param tGroupId 租户组ID
     * @param page 分页参数
     * @return 分页的 GroupBindDeptVO 列表
     */
    @Override
    public IPage<GroupBindDeptVO> getGroupBindDeptsBySGroupId(Long tGroupId, Page<GroupBindDept> page) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getSGroupId, tGroupId)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        IPage<GroupBindDept> dataPage = this.page(page, queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<GroupBindDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据租户组ID列表分页查询绑定关系，并按组ID分组返回
     *
     * @param tGroupIds 租户组ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 sGroupId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupBindDeptVO>>> getGroupBindDeptsBySGroupIds(List<Long> tGroupIds, Page<GroupBindDept> page) {
        return FetchUtils.pageGroupQuery(
                tGroupIds,
                page,
                wrapper -> wrapper.in(GroupBindDept::getSGroupId, tGroupIds)
                        .eq(GroupBindDept::getDeleted, false)
                        .orderByDesc(GroupBindDept::getCreatedAt),
                this::page,
                groupBindDeptConverter::EnListToVOList,
                GroupBindDeptVO::getTGroupId
        );
    }

    /**
     * 根据租户部门ID分页查询关联的组
     *
     * @param tDeptId 租户部门ID
     * @param page 分页参数
     * @return 分页的 GroupBindDeptVO 列表
     */
    @Override
    public IPage<GroupBindDeptVO> getGroupBindDeptsByTDeptId(Long tDeptId, Page<GroupBindDept> page) {
        LambdaQueryWrapper<GroupBindDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupBindDept::getTDeptId, tDeptId)
                .eq(GroupBindDept::getDeleted, false)
                .orderByDesc(GroupBindDept::getCreatedAt);
        IPage<GroupBindDept> dataPage = this.page(page, queryWrapper);
        List<GroupBindDeptVO> voList = groupBindDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<GroupBindDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据租户部门ID列表分页查询绑定关系，并按部门ID分组返回
     *
     * @param tDeptIds 租户部门ID列表
     * @param page 分页参数
     * @return 分页的 Map，键为 tDeptId，值为对应 VO 列表
     */
    @Override
    public IPage<Map<Long, List<GroupBindDeptVO>>> getGroupBindDeptsByTDeptIds(List<Long> tDeptIds, Page<GroupBindDept> page) {
        return FetchUtils.pageGroupQuery(
                tDeptIds,
                page,
                wrapper -> wrapper.in(GroupBindDept::getTDeptId, tDeptIds)
                        .eq(GroupBindDept::getDeleted, false)
                        .orderByDesc(GroupBindDept::getCreatedAt),
                this::page,
                groupBindDeptConverter::EnListToVOList,
                GroupBindDeptVO::getTDeptId
        );
    }

    /**
     * 新增组-租户部门绑定
     *
     * @param groupBindDeptRTO 请求传输对象，包含绑定信息
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createGroupBindDept(GroupBindDeptRTO groupBindDeptRTO) {
        GroupBindDept entity = new GroupBindDept();
        BeanUtils.copyProperties(groupBindDeptRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getDeleted() == null) {
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新组-租户部门绑定
     *
     * @param groupBindDeptRTO 请求传输对象，包含更新信息，ID不能为空
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGroupBindDept(GroupBindDeptRTO groupBindDeptRTO) {
        if (groupBindDeptRTO.getId() == null) {
            return false;
        }
        GroupBindDept entity = new GroupBindDept();
        BeanUtils.copyProperties(groupBindDeptRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<GroupBindDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupBindDept::getId, groupBindDeptRTO.getId())
                .eq(GroupBindDept::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 删除组-租户部门绑定（物理删除）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroupBindDept(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除组-租户部门绑定（标记 deleted = true）
     *
     * @param id 绑定记录ID
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteGroupBindDept(Long id) {
        LambdaUpdateWrapper<GroupBindDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(GroupBindDept::getId, id)
                .eq(GroupBindDept::getDeleted, false)
                .set(GroupBindDept::getDeleted, true)
                .set(GroupBindDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除组-租户部门绑定（物理删除）
     *
     * @param ids 绑定记录ID列表
     * @return 全部删除成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteGroupBindDepts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除组-租户部门绑定（标记 deleted = true）
     *
     * @param ids 绑定记录ID列表
     * @return 成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteGroupBindDepts(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<GroupBindDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(GroupBindDept::getId, ids)
                .eq(GroupBindDept::getDeleted, false)
                .set(GroupBindDept::getDeleted, true)
                .set(GroupBindDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }
    
}
