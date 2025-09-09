package com.cloudvalley.nebula.monolith.shared.api.combo.service;

import com.cloudvalley.nebula.monolith.shared.api.combo.model.vo.ComboQuotaVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IComboQuotaCommonService {

    /**
     * 根据 id 查询套餐配额配置（单个）
     * @param id 配额配置ID
     * @return 配额配置
     */
    ComboQuotaVO getComboQuotaById(Long id);

    /**
     * 根据 id 批量查询套餐配额配置 [全量]
     * @param ids 配额配置ID列表
     * @return 配额配置信息
     */
    List<ComboQuotaVO> getComboQuotasByIds(List<Long> ids);

    /**
     * 根据系统套餐 id 查询配额配置 [全量]
     * @param sComboId 系统套餐ID
     * @return 配额配置信息
     */
    List<ComboQuotaVO> getComboQuotasBySComboId(Long sComboId);

    /**
     * 根据系统套餐 id 批量查询配额配置 [全量] - 批量返回分组结果
     * @param sComboIds 系统套餐ID列表
     * @return 分组结果
     */
    List<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySComboIds(List<Long> sComboIds);

    /**
     * 根据系统配额 id 查询配额配置 [全量]
     * @param sQuotaId 系统配额ID
     * @return 配额配置信息
     */
    List<ComboQuotaVO> getComboQuotasBySQuotaId(Long sQuotaId);

    /**
     * 根据系统配额 id 批量查询配额配置 [全量] - 批量返回分组结果
     * @param sQuotaIds 系统配额ID列表
     * @return 分组结果
     */
    List<Map<Long, List<ComboQuotaVO>>> getComboQuotasBySQuotaIds(List<Long> sQuotaIds);

    /**
     * 根据系统套餐 ID 查询系统配额 ID 列表
     * @param sComboId 系统套餐ID
     * @return 系统配额ID集合
     */
    Set<Long> getSQuotaIdsBySComboId(Long sComboId);

    /**
     * 根据系统配额 ID 查询系统套餐 ID 列表
     * @param sQuotaId 系统配额ID
     * @return 系统套餐ID集合
     */
    Set<Long> getSComboIdsBySQuotaId(Long sQuotaId);

}
