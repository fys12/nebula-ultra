package com.cloudvalley.nebula.ultra.business.tenant.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.TenantSubscribe;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.tenant.model.rto.TenantSubscribeRTO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.TenantSubscribeVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户订阅的套餐 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ITenantSubscribeService extends IService<TenantSubscribe> {

    /**
     * 查询租户订阅列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<TenantSubscribeVO> getTenantSubscribeList(Page<TenantSubscribe> page);

    /**
     * 根据id查询订阅(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 订阅ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<TenantSubscribeVO> getTenantSubscribesByIds(List<Long> ids, Page<TenantSubscribe> page);

    /**
     * 根据租户id查询订阅(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param tenantId 租户ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<TenantSubscribeVO> getTenantSubscribesByTenantId(Long tenantId, Page<TenantSubscribe> page);

    /**
     * 根据租户id查询订阅(批量查询多个租户)[分页]
     * @param tenantIds 租户ID列表
     * @param page 分页参数
     * @return 分页结果，按租户ID分组
     */
    IPage<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByTenantIds(List<Long> tenantIds, Page<TenantSubscribe> page);

    /**
     * 根据套餐id查询订阅(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param comboId 套餐ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<TenantSubscribeVO> getTenantSubscribesByComboId(Long comboId, Page<TenantSubscribe> page);

    /**
     * 根据套餐id查询订阅(批量查询多个套餐)[分页]
     * @param comboIds 套餐ID列表
     * @param page 分页参数
     * @return 分页结果，按套餐ID分组
     */
    IPage<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByComboIds(List<Long> comboIds, Page<TenantSubscribe> page);

    /**
     * 新增订阅
     * @param tenantSubscribeRTO 订阅信息
     * @return 是否成功
     */
    boolean createTenantSubscribe(TenantSubscribeRTO tenantSubscribeRTO);

    /**
     * 更新订阅
     * @param tenantSubscribeRTO 订阅信息
     * @return 是否成功
     */
    boolean updateTenantSubscribe(TenantSubscribeRTO tenantSubscribeRTO);

    /**
     * 更新订阅状态
     * @param id 订阅ID
     * @param status 状态
     * @return 是否成功
     */
    boolean updateTenantSubscribeStatus(Long id, String status);

    /**
     * 删除订阅（真删）
     * @param id 订阅ID
     * @return 是否成功
     */
    boolean deleteTenantSubscribe(Long id);

    /**
     * 软删除订阅
     * @param id 订阅ID
     * @return 是否成功
     */
    boolean softDeleteTenantSubscribe(Long id);

    /**
     * 批量删除订阅（真删）
     * @param ids 订阅ID列表
     * @return 是否成功
     */
    boolean batchDeleteTenantSubscribes(List<Long> ids);

    /**
     * 批量软删除订阅
     * @param ids 订阅ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteTenantSubscribes(List<Long> ids);

}
