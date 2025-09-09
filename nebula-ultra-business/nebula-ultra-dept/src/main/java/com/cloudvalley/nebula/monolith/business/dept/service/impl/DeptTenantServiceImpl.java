package com.cloudvalley.nebula.monolith.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.converter.DeptTenantConverter;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.DeptTenant;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.DeptTenantRTO;
import com.cloudvalley.nebula.monolith.business.dept.mapper.DeptTenantMapper;
import com.cloudvalley.nebula.monolith.business.dept.service.IDeptTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.DeptTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户-部门绑定 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class DeptTenantServiceImpl extends ServiceImpl<DeptTenantMapper, DeptTenant> implements IDeptTenantService {

    @Autowired
    private DeptTenantConverter tenantDeptConverter;

    /**
     * 查询租户 - 部门绑定列表 [分页]
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<DeptTenantVO> 分页结果集（包含租户部门绑定VO列表、总条数、当前页、每页条数）
     */
    @Override
    public IPage<DeptTenantVO> getDeptTenantList(Page<DeptTenant> page) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        IPage<DeptTenant> dataPage = this.page(page, queryWrapper);
        List<DeptTenantVO> voList = tenantDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<DeptTenantVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据租户 - 部门绑定 id 批量查询绑定关系 [分页]
     * @param ids 租户部门绑定主键ID列表（雪花算法ID）
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<DeptTenantVO> 分页结果集（包含租户部门绑定VO列表、总条数、当前页、每页条数；ids为空时返回空分页）
     */
    @Override
    public IPage<DeptTenantVO> getDeptTenantsByIds(List<Long> ids, Page<DeptTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptTenant::getId, ids)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        IPage<DeptTenant> dataPage = this.page(page, queryWrapper);
        List<DeptTenantVO> voList = tenantDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<DeptTenantVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统租户 id 查询绑定关系 [分页]
     * @param sTenantId 系统租户主键ID（雪花算法ID）
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<DeptTenantVO> 分页结果集（包含租户部门绑定VO列表、总条数、当前页、每页条数）
     */
    @Override
    public IPage<DeptTenantVO> getDeptTenantsBySTenantId(Long sTenantId, Page<DeptTenant> page) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSTenantId, sTenantId)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        IPage<DeptTenant> dataPage = this.page(page, queryWrapper);
        List<DeptTenantVO> voList = tenantDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<DeptTenantVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统租户 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sTenantIds 系统租户主键ID列表（雪花算法ID）
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<Map<Long, List<DeptTenantVO>>> 分页分组结果集（key：系统租户ID，value：对应租户的部门绑定VO列表）
     */
    @Override
    public IPage<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySTenantIds(List<Long> sTenantIds, Page<DeptTenant> page) {
        return FetchUtils.pageGroupQuery(
                sTenantIds,
                page,
                wrapper -> wrapper.in(DeptTenant::getSTenantId, sTenantIds)
                        .eq(DeptTenant::getDeleted, false)
                        .orderByDesc(DeptTenant::getCreatedAt),
                this::page,
                tenantDeptConverter::EnListToVOList,
                DeptTenantVO::getSTenantId
        );
    }

    /**
     * 根据系统部门 id 查询绑定关系 [分页]
     * @param sDeptId 系统部门主键ID（雪花算法ID）
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<DeptTenantVO> 分页结果集（包含租户部门绑定VO列表、总条数、当前页、每页条数）
     */
    @Override
    public IPage<DeptTenantVO> getDeptTenantsBySDeptId(Long sDeptId, Page<DeptTenant> page) {
        LambdaQueryWrapper<DeptTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptTenant::getSDeptId, sDeptId)
                .eq(DeptTenant::getDeleted, false)
                .orderByDesc(DeptTenant::getCreatedAt);
        IPage<DeptTenant> dataPage = this.page(page, queryWrapper);
        List<DeptTenantVO> voList = tenantDeptConverter.EnListToVOList(dataPage.getRecords());
        return new Page<DeptTenantVO>()
                .setRecords(voList)
                .setTotal(dataPage.getTotal())
                .setCurrent(dataPage.getCurrent())
                .setSize(dataPage.getSize());
    }

    /**
     * 根据系统部门 id 批量查询绑定关系 [分页] - 返回分组结果
     * @param sDeptIds 系统部门主键ID列表（雪花算法ID）
     * @param page 分页参数（包含当前页、每页条数等信息）
     * @return IPage<Map<Long, List<DeptTenantVO>>> 分页分组结果集（key：系统部门ID，value：对应部门的租户绑定VO列表）
     */
    @Override
    public IPage<Map<Long, List<DeptTenantVO>>> getDeptTenantsBySDeptIds(List<Long> sDeptIds, Page<DeptTenant> page) {
        return FetchUtils.pageGroupQuery(
                sDeptIds,
                page,
                wrapper -> wrapper.in(DeptTenant::getSDeptId, sDeptIds)
                        .eq(DeptTenant::getDeleted, false)
                        .orderByDesc(DeptTenant::getCreatedAt),
                this::page,
                tenantDeptConverter::EnListToVOList,
                DeptTenantVO::getSDeptId
        );
    }

    /**
     * 新增租户 - 部门绑定
     * @param tenantDeptRTO 租户部门绑定新增请求参数对象（包含租户ID、部门ID等核心信息）
     * @return boolean 新增结果（true：成功，false：失败）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDeptTenant(DeptTenantRTO tenantDeptRTO) {
        DeptTenant entity = new DeptTenant();
        BeanUtils.copyProperties(tenantDeptRTO, entity);
        entity.setId(GeneratorUtils.generateId());
        entity.setCreatedAt(GeneratorUtils.generateCurrentTime());
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        entity.setCreatedById(FetchUtils.getCurrentUserId());
        if (entity.getState() == null && entity.getDeleted() == null) {
            entity.setState(true);
            entity.setDeleted(false);
        }
        return this.save(entity);
    }

    /**
     * 更新租户 - 部门绑定
     * @param tenantDeptRTO 租户部门绑定更新请求参数对象（必须包含主键ID，否则更新失败）
     * @return boolean 更新结果（true：成功，false：失败）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptTenant(DeptTenantRTO tenantDeptRTO) {
        if (tenantDeptRTO.getId() == null) {
            return false;
        }
        DeptTenant entity = new DeptTenant();
        BeanUtils.copyProperties(tenantDeptRTO, entity);
        entity.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        LambdaUpdateWrapper<DeptTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptTenant::getId, tenantDeptRTO.getId())
                .eq(DeptTenant::getDeleted, false);
        return this.update(entity, updateWrapper);
    }

    /**
     * 更新绑定状态（该部门在该租户中的启用状态）
     * @param id 租户部门绑定主键ID（雪花算法ID）
     * @param state 目标状态（true：启用，false：禁用）
     * @return boolean 更新结果（true：成功，false：失败）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptTenantState(Long id, Boolean state) {
        LambdaUpdateWrapper<DeptTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptTenant::getId, id)
                .eq(DeptTenant::getDeleted, false)
                .set(DeptTenant::getState, state)
                .set(DeptTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 删除租户 - 部门绑定（真删）
     * @param id 租户部门绑定主键ID（雪花算法ID）
     * @return boolean 删除结果（true：成功，false：失败）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeptTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除租户 - 部门绑定
     * 说明：软删除仅更新数据的删除状态（deleted字段设为true），不实际删除数据库记录，保留数据追溯能力
     * @param id 租户-部门绑定关系的主键ID（雪花算法ID，用于定位待删除的绑定记录）
     * @return boolean 软删除操作结果（true：删除成功，false：删除失败，如未找到对应未删除的绑定记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteDeptTenant(Long id) {
        LambdaUpdateWrapper<DeptTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptTenant::getId, id)
                .eq(DeptTenant::getDeleted, false)
                .set(DeptTenant::getDeleted, true)
                .set(DeptTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }

    /**
     * 批量删除租户 - 部门绑定（真删）
     * 说明：物理删除会直接从数据库中移除记录，删除后无法恢复，需谨慎使用
     * @param ids 租户-部门绑定关系的主键ID列表（雪花算法ID集合，用于定位待批量删除的绑定记录）
     * @return boolean 批量物理删除操作结果（true：删除成功，false：删除失败，如ID列表为空或无对应记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDeptTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除租户 - 部门绑定
     * 说明：批量软删除仅批量更新数据的删除状态（deleted字段设为true），不实际删除数据库记录，保留数据追溯能力
     * @param ids 租户-部门绑定关系的主键ID列表（雪花算法ID集合，用于定位待批量软删除的绑定记录）
     * @return boolean 批量软删除操作结果（true：删除成功，false：删除失败，如ID列表为空或无对应未删除的记录）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteDeptTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        LambdaUpdateWrapper<DeptTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DeptTenant::getId, ids)
                .eq(DeptTenant::getDeleted, false)
                .set(DeptTenant::getDeleted, true)
                .set(DeptTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());
        return this.update(updateWrapper);
    }
    
}
