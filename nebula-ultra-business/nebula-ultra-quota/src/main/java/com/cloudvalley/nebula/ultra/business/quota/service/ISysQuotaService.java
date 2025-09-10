package com.cloudvalley.nebula.ultra.business.quota.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.quota.model.entity.SysQuota;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.quota.model.rto.SysQuotaRTO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;

import java.util.List;

/**
 * <p>
 * 系统配额 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface ISysQuotaService extends IService<SysQuota> {

    /**
     * 查询系统配额列表 [分页]
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysQuotaVO> getSysQuotaList(Page<SysQuota> page);

    /**
     * 根据id查询系统配额(可查询单个或多个，传入的id可以单个或多个)[分页]
     * @param ids 系统配额ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<SysQuotaVO> getSysQuotasByIds(List<Long> ids, Page<SysQuota> page);

    /**
     * 新增系统配额（定义配额类型，如存储、AI tokens）
     * @param sysQuotaRTO 系统配额信息
     * @return 是否成功
     */
    boolean createSysQuota(SysQuotaRTO sysQuotaRTO);

    /**
     * 更新系统配额（如名称、单位、描述）
     * @param sysQuotaRTO 系统配额信息
     * @return 是否成功
     */
    boolean updateSysQuota(SysQuotaRTO sysQuotaRTO);

    /**
     * 更新系统配额状态（启用/禁用）
     * @param id 系统配额ID
     * @param state 状态
     * @return 是否成功
     */
    boolean updateSysQuotaState(Long id, Boolean state);

    /**
     * 删除系统配额（真删）
     * @param id 系统配额ID
     * @return 是否成功
     */
    boolean deleteSysQuota(Long id);

    /**
     * 软删除系统配额
     * @param id 系统配额ID
     * @return 是否成功
     */
    boolean softDeleteSysQuota(Long id);

    /**
     * 批量删除系统配额（真删）
     * @param ids 系统配额ID列表
     * @return 是否成功
     */
    boolean batchDeleteSysQuotas(List<Long> ids);

    /**
     * 批量软删除系统配额
     * @param ids 系统配额ID列表
     * @return 是否成功
     */
    boolean batchSoftDeleteSysQuotas(List<Long> ids);

}
