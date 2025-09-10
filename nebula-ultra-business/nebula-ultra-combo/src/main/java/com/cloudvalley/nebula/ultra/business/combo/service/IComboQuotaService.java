package com.cloudvalley.nebula.ultra.business.combo.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboQuota;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloudvalley.nebula.ultra.business.combo.model.rto.ComboQuotaRTO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboQuotaVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 套餐配额配置 服务类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
public interface IComboQuotaService extends IService<ComboQuota> {

    /**
     * 查询套餐配额配置列表 [分页]
     * @param page 分页参数
     * @return 配额配置列表
     */
    IPage<ComboQuotaVO> getComboQuotaList(Page<ComboQuota> page);

    /**
     * 根据 id 批量查询套餐配额配置 [分页]
     * @param ids 配额配置ID列表
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboQuotaVO> getComboQuotasByIds(List<Long> ids, Page<ComboQuota> page);

    /**
     * 根据系统套餐 id 查询配额配置 [分页]
     * @param sComboId 系统套餐ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboQuotaVO> getComboQuotasBySComboId(Long sComboId, Page<ComboQuota> page);

    /**
     * 根据系统套餐 id 批量查询配额配置 [分页] - 返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @param page 分页参数
     * @return 分组分页结果
     */
    IPage<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySComboIds(List<Long> sComboIds, Page<ComboQuota> page);

    /**
     * 根据系统配额 id 查询配额配置 [分页]
     * @param sQuotaId 系统配额ID
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ComboQuotaVO> getComboQuotasBySQuotaId(Long sQuotaId, Page<ComboQuota> page);

    /**
     * 根据系统配额 id 批量查询配额配置 [分页] - 返回分组结果
     * @param sQuotaIds 系统配额ID列表
     * @param page 分页参数
     * @return 分组分页结果
     */
    IPage<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySQuotaIds(List<Long> sQuotaIds, Page<ComboQuota> page);

    /**
     * 新增套餐配额配置
     */
    boolean createComboQuota(ComboQuotaRTO comboQuotaRTO);

    /**
     * 更新套餐配额配置
     */
    boolean updateComboQuota(ComboQuotaRTO comboQuotaRTO);

    /**
     * 删除套餐配额配置（真删）
     */
    boolean deleteComboQuota(Long id);

    /**
     * 软删除套餐配额配置
     */
    boolean softDeleteComboQuota(Long id);

    /**
     * 批量删除套餐配额配置（真删）
     */
    boolean batchDeleteComboQuotas(List<Long> ids);

    /**
     * 批量软删除套餐配额配置
     */
    boolean batchSoftDeleteComboQuotas(List<Long> ids);

}
