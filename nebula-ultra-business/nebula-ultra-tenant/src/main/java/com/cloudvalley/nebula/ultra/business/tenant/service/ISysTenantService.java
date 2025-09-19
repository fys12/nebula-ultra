package com.cloudvalley.nebula.ultra.business.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.tenant.model.rto.SysTenantRTO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;

import java.util.List;

/**
 * <p>
 * 租户 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysTenantService extends IService<SysTenant> {

    /**
     * 查询系统租户列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysTenantVO> getSysTenantList(Page<SysTenant> page);

    /**
     * 根据id查询系统租户(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 系统租户ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysTenantVO> getSysTenantsByIds(List<Long> ids, Page<SysTenant> page);

    /**
     * 新增系统租户
     * @param sysTenantRTO 系统租户信息
     * @return 是否成功
     */
    boolean createSysTenant(SysTenantRTO sysTenantRTO);

    /**
     * 更新系统租户
     * @param sysTenantRTO 系统租户信息
     * @return 是否成功
     */
    boolean updateSysTenant(SysTenantRTO sysTenantRTO);

    /**
     * 更新系统租户状态
     * @param id 系统租户ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateSysTenantState(Long id, Boolean state);

    /**
     * 删除系统租户（真删）
     * @param id 系统租户ID
     * @return 是否成功
     */
    boolean deleteSysTenant(Long id);

    /**
     * 软删除系统租户
     * @param id 系统租户ID
     * @return 是否成功
     */
    boolean softDeleteSysTenant(Long id);

    /**
     * 批量删除系统租户（真删）
     * @param ids 系统租户ID列表
     * @return 是否成功
     */
    boolean batchDeleteSysTenants(List<Long> ids);

    /**
     * 批量软删除系统租户
     * @param ids 系统租户ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteSysTenants(List<Long> ids);

    /**
     * 根据 租户Id 查询 子租户
     * @param id 系统租户Id
     * @return 子租户Id列表
     */
    List<SysTenant> getChildTenantIds(Long id);

}
