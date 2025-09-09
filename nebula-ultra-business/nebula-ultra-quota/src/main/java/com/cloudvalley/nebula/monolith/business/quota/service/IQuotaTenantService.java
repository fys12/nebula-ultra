package com.cloudvalley.nebula.monolith.business.quota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.quota.model.entity.QuotaTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.monolith.business.quota.model.rto.QuotaTenantRTO;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.QuotaTenantVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户配额总览 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IQuotaTenantService extends IService<QuotaTenant> {

    /**
     * 查询租户配额列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<QuotaTenantVO> getQuotaTenantList(Page<QuotaTenant> page);


    /**
     * 根据id查询配额(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 配额ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<QuotaTenantVO> getQuotaTenantsByIds(List<Long> ids, Page<QuotaTenant> page);

    /**
     * 根据系统租户id查询配额(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param tenantId 系统租户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<QuotaTenantVO> getQuotaTenantsByTenantId(Long tenantId, Page<QuotaTenant> page);

    /**
     * 根据系统租户id查询配额(批量查询多个租户)[分页]
     * @param tenantIds 系统租户ID列表
     * @param page 分页参数
     * @return 分页结果，按租户ID分组
     */
    IPage<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByTenantIds(List<Long> tenantIds, Page<QuotaTenant> page);

    /**
     * 根据系统配额id查询配额(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param quotaId 系统配额ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<QuotaTenantVO> getQuotaTenantsByQuotaId(Long quotaId, Page<QuotaTenant> page);

    /**
     * 根据系统配额id查询配额(批量查询多个配额)[分页]
     * @param quotaIds 系统配额ID列表
     * @param page 分页参数
     * @return 分页结果，按配额ID分组
     */
    IPage<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByQuotaIds(List<Long> quotaIds, Page<QuotaTenant> page);

    /**
     * 新增租户配额
     * @param quotaTenantRTO 租户配额信息
     * @return 是否成功
     */
    boolean createQuotaTenant(QuotaTenantRTO quotaTenantRTO);

    /**
     * 更新租户配额
     * @param quotaTenantRTO 租户配额信息
     * @return 是否成功
     */
    boolean updateQuotaTenant(QuotaTenantRTO quotaTenantRTO);

    /**
     * 删除配额（真删）
     * @param id 配额ID
     * @return 是否成功
     */
    boolean deleteQuotaTenant(Long id);

    /**
     * 软删除配额
     * @param id 配额ID
     * @return 是否成功
     */
    boolean softDeleteQuotaTenant(Long id);

    /**
     * 批量删除配额（真删）
     * @param ids 配额ID列表
     * @return 是否成功
     */
    boolean batchDeleteQuotaTenants(List<Long> ids);

    /**
     * 批量软删除配额
     * @param ids 配额ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteQuotaTenants(List<Long> ids);
    
}
