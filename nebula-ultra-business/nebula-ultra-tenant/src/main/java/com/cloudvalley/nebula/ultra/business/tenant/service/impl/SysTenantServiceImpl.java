package com.cloudvalley.nebula.ultra.business.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.converter.SysTenantConverter;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.cloudvalley.nebula.ultra.business.tenant.mapper.SysTenantMapper;
import com.cloudvalley.nebula.ultra.business.tenant.model.rto.SysTenantRTO;
import com.cloudvalley.nebula.ultra.business.tenant.service.ISysTenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.common.utils.FetchUtils;
import com.cloudvalley.nebula.ultra.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 租户 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    /**
     * 分页查询系统租户列表
     * @param page 分页参数对象（当前页、页大小）
     * @return 分页的 SysTenantVO 列表，仅包含未软删记录，按创建时间倒序排列
     */
    @Override
    public IPage<SysTenantVO> getSysTenantList(Page<SysTenant> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenant::getDeleted, false)
                .orderByDesc(SysTenant::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<SysTenant> sysTenantPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysTenantVO> voList = sysTenantConverter.EnListToVOList(sysTenantPage.getRecords());

        // 创建新的 IPage<SysTenantVO>，复用原分页参数
        return new Page<SysTenantVO>()
                .setRecords(voList)
                .setTotal(sysTenantPage.getTotal())
                .setCurrent(sysTenantPage.getCurrent())
                .setSize(sysTenantPage.getSize());
    }

    /**
     * 根据多个ID分页批量查询系统租户
     * @param ids 系统租户ID列表
     * @param page 分页参数对象
     * @return 分页的 SysTenantVO 列表，仅返回未软删记录；ids为空时返回空分页
     */
    @Override
    public IPage<SysTenantVO> getSysTenantsByIds(List<Long> ids, Page<SysTenant> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenant::getId, ids)
                .eq(SysTenant::getDeleted, false)
                .orderByDesc(SysTenant::getCreatedAt);

        IPage<SysTenant> sysTenantPage = this.page(page, queryWrapper);
        List<SysTenantVO> voList = sysTenantConverter.EnListToVOList(sysTenantPage.getRecords());

        return new Page<SysTenantVO>()
                .setRecords(voList)
                .setTotal(sysTenantPage.getTotal())
                .setCurrent(sysTenantPage.getCurrent())
                .setSize(sysTenantPage.getSize());
    }

    /**
     * 新增系统租户
     * @param sysTenantRTO 请求传输对象，包含租户编码、名称、联系人、联系方式等信息
     * @return 成功返回 true，失败返回 false；生成ID与时间戳，设置默认状态和创建人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createSysTenant(SysTenantRTO sysTenantRTO) {
        SysTenant sysTenant = new SysTenant();
        BeanUtils.copyProperties(sysTenantRTO, sysTenant);

        // 设置默认值
        sysTenant.setId(GeneratorUtils.generateId());
        sysTenant.setCreatedAt(GeneratorUtils.generateCurrentTime());
        sysTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        sysTenant.setCreatedById(FetchUtils.getCurrentUserId());
        if (sysTenantRTO.getState() == null && sysTenant.getDeleted() == null) {
            sysTenant.setState(true);
            sysTenant.setDeleted(false);
        }

        return this.save(sysTenant);
    }

    /**
     * 更新系统租户信息
     * @param sysTenantRTO 包含 ID 和需更新字段的传输对象
     * @return 成功返回 true，失败返回 false；仅更新未软删记录，刷新更新时间与操作人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysTenant(SysTenantRTO sysTenantRTO) {
        if (sysTenantRTO.getId() == null) {
            return false;
        }

        SysTenant sysTenant = new SysTenant();
        BeanUtils.copyProperties(sysTenantRTO, sysTenant);
        sysTenant.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<SysTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysTenant::getId, sysTenantRTO.getId())
                .eq(SysTenant::getDeleted, false);

        return this.update(sysTenant, updateWrapper);
    }

    /**
     * 更新系统租户状态（启用/禁用）
     * @param id 系统租户ID
     * @param state 目标状态：true 启用，false 禁用
     * @return 操作成功返回 true，否则返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateSysTenantState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysTenant::getId, id)
                .eq(SysTenant::getDeleted, false)
                .set(SysTenant::getState, state)
                .set(SysTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 删除系统租户（物理删除）
     * @param id 系统租户ID
     * @return 成功返回 true，失败返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSysTenant(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统租户（标记 deleted = true）
     * @param id 系统租户ID
     * @return 成功返回 true，失败返回 false；仅作用于未软删记录
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteSysTenant(Long id) {
        LambdaUpdateWrapper<SysTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysTenant::getId, id)
                .eq(SysTenant::getDeleted, false)
                .set(SysTenant::getDeleted, true)
                .set(SysTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量删除多个系统租户（物理删除）
     * @param ids 系统租户ID列表
     * @return 全部删除成功返回 true，否则返回 false；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteSysTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个系统租户（标记 deleted = true）
     * @param ids 系统租户ID列表
     * @return 成功返回 true，否则返回 false；仅处理未软删记录；空列表返回 false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteSysTenants(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysTenant> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysTenant::getId, ids)
                .eq(SysTenant::getDeleted, false)
                .set(SysTenant::getDeleted, true)
                .set(SysTenant::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 根据 租户Id 查询 子租户
     * @param id 系统租户Id
     * @return 子租户Id列表
     */
    @Override
    public List<SysTenant> getChildTenantIds(Long id) {
        LambdaUpdateWrapper<SysTenant> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(SysTenant::getParentId, id);
        return this.list(queryWrapper);
    }

}
