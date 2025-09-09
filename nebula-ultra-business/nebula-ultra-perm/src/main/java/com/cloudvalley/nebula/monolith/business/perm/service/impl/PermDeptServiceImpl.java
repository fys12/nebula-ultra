package com.cloudvalley.nebula.monolith.business.perm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.perm.converter.PermDeptConverter;
import com.cloudvalley.nebula.monolith.business.perm.model.entity.PermDept;
import com.cloudvalley.nebula.monolith.business.perm.mapper.PermDeptMapper;
import com.cloudvalley.nebula.monolith.business.perm.model.rto.PermDeptRTO;
import com.cloudvalley.nebula.monolith.business.perm.service.IPermDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.perm.model.vo.PermDeptVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户部门-租户权限关联 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class PermDeptServiceImpl extends ServiceImpl<PermDeptMapper, PermDept> implements IPermDeptService {

    @Autowired
    private PermDeptConverter permDeptConverter;

    /**
     * 分页查询租户部门-租户权限关联列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 PermDeptVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<PermDeptVO> getPermDeptList(Page<PermDept> page) {
        // 仅取未软删的数据，符合全局查询约束
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt); // 新数据优先
        IPage<PermDept> dataPage = this.page(page, queryWrapper);
        // 分页查询 + 实体转 VO
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(dataPage.getRecords());
        // 复用原分页元数据，替换记录类型
        return new Page<PermDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据多个ID分页批量查询租户部门-租户权限关联
     * @param ids 关联ID列表
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，仅返回未软删记录，按创建时间倒序排列；ids为空时返回空分页
     */
    @Override
    public IPage<PermDeptVO> getPermDeptsByIds(List<Long> ids, Page<PermDept> page) {
        // 空参快速返回空分页，避免不必要查询
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PermDept::getId, ids)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        IPage<PermDept> dataPage = this.page(page, queryWrapper);
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<PermDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据租户部门ID分页查询其关联的权限列表
     * @param tDeptId 租户部门ID
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，表示该部门拥有的权限，按创建时间倒序排列
     */
    @Override
    public IPage<PermDeptVO> getPermDeptsByTDeptId(Long tDeptId, Page<PermDept> page) {
        // 指定部门拥有哪些权限，排除软删
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTDeptId, tDeptId)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        IPage<PermDept> dataPage = this.page(page, queryWrapper);
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<PermDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据多个租户部门ID分页批量查询关联权限，并按部门ID分组返回
     * @param tDeptIds 租户部门ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tDeptId，值为对应部门的 PermDeptVO 列表
     */
    @Override
    public IPage<Map<Long, List<PermDeptVO>>> getPermDeptsByTDeptIds(List<Long> tDeptIds, Page<PermDept> page) {
        return FetchUtils.pageGroupQuery(
                tDeptIds,
                page,
                wrapper -> wrapper.in(PermDept::getTDeptId, tDeptIds)
                        .eq(PermDept::getDeleted, false)
                        .orderByDesc(PermDept::getCreatedAt),
                this::page,
                permDeptConverter::EnListToVOList,
                PermDeptVO::getTDeptId
        );
    }

    /**
     * 根据租户权限ID分页查询使用该权限的部门列表
     * @param tPermId 租户权限ID
     * @param page 分页参数对象
     * @return 分页的 PermDeptVO 列表，表示拥有该权限的部门，按创建时间倒序排列
     */
    @Override
    public IPage<PermDeptVO> getPermDeptsByTPermId(Long tPermId, Page<PermDept> page) {
        // 指定权限被哪些部门拥有，排除软删
        LambdaQueryWrapper<PermDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PermDept::getTPermId, tPermId)
                .eq(PermDept::getDeleted, false)
                .orderByDesc(PermDept::getCreatedAt);
        IPage<PermDept> dataPage = this.page(page, queryWrapper);
        List<PermDeptVO> voList = permDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<PermDeptVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据多个租户权限ID分页批量查询使用部门，并按权限ID分组返回
     * @param tPermIds 租户权限ID列表
     * @param page 分页参数对象
     * @return 分页的 Map 列表，键为 tPermId，值为对应权限的部门VO列表
     */
    @Override
    public IPage<Map<Long, List<PermDeptVO>>> getPermDeptsByTPermIds(List<Long> tPermIds, Page<PermDept> page) {
        return FetchUtils.pageGroupQuery(
                tPermIds,
                page,
                wrapper -> wrapper.in(PermDept::getTPermId, tPermIds)
                        .eq(PermDept::getDeleted, false)
                        .orderByDesc(PermDept::getCreatedAt),
                this::page,
                permDeptConverter::EnListToVOList,
                PermDeptVO::getTPermId
        );
    }

    /**
     * 新增租户部门-租户权限关联
     * @param permDeptRTO 请求传输对象，包含 tDeptId 和 tPermId
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPermDept(PermDeptRTO permDeptRTO) {
        // RTO -> Entity，统一 Bean 拷贝
        PermDept entity = new PermDept();
        BeanUtils.copyProperties(permDeptRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        if (entity.getState() == null && entity.getDeleted() == null) {
            entity.setState(true);
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新租户部门-租户权限关联
     * @param permDeptRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermDept(PermDeptRTO permDeptRTO) {
        // 必须包含主键 ID
        if (permDeptRTO.getId() == null) {
            return false;
        }
        PermDept entity = new PermDept();
        BeanUtils.copyProperties(permDeptRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        // 仅更新未软删记录，避免误改历史数据
        LambdaUpdateWrapper<PermDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermDept::getId, permDeptRTO.getId())
                .eq(PermDept::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 更新关联关系状态（启用/禁用权限在部门中的生效）
     * @param id 关联ID
     * @param status 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePermDeptStatus(Long id, Boolean status) {
        LambdaUpdateWrapper<PermDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermDept::getId, id)
                .eq(PermDept::getDeleted, false)
                .set(PermDept::getState, status)
                .set(PermDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 删除关联关系（物理删除）
     * @param id 关联ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deletePermDept(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除关联关系（标记 deleted = true）
     * @param id 关联ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeletePermDept(Long id) {
        // 软删：标记 deleted=true 并更新时间；仅作用于未软删记录
        LambdaUpdateWrapper<PermDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(PermDept::getId, id)
                .eq(PermDept::getDeleted, false)
                .set(PermDept::getDeleted, true)
                .set(PermDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除关联关系（物理删除）
     * @param ids 关联ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeletePermDepts(List<Long> ids) {
        // 空集合短路，避免误操作
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除关联关系（标记 deleted = true）
     * @param ids 关联ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeletePermDepts(List<Long> ids) {
        // 空集合短路，避免误操作；仅处理未软删记录
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<PermDept> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(PermDept::getId, ids)
                .eq(PermDept::getDeleted, false)
                .set(PermDept::getDeleted, true)
                .set(PermDept::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

}
