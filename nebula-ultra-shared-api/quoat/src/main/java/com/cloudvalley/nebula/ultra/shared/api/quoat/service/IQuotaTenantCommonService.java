package com.cloudvalley.nebula.ultra.shared.api.quoat.service;

import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.QuotaTenantVO;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IQuotaTenantCommonService {

    /**
     * 根据租户配额ID查询单个绑定信息
     * @param id 租户配额关联的唯一标识ID
     * @return 对应的 QuotaTenantVO 对象，若不存在或已软删则返回 null
     */
    QuotaTenantVO getQuotaTenantById(Long id);

    /**
     * 根据多个租户配额ID全量查询绑定信息（不分页）
     * @param ids 租户配额关联ID列表
     * @return 所有匹配的 QuotaTenantVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<QuotaTenantVO> getQuotaTenantsByIds(List<Long> ids);

    /**
     * 根据系统租户ID全量查询其绑定的配额列表（查看某租户拥有哪些系统配额）
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的所有 QuotaTenantVO 列表，按创建时间倒序排列
     */
    List<QuotaTenantVO> getQuotaTenantsByTenantId(Long tenantId);

    /**
     * 根据多个系统租户ID全量查询绑定配额，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 QuotaTenantVO 列表；输入为空时返回空列表
     */
    Map<Long, List<QuotaTenantVO>> getQuotaTenantsByTenantIds(List<Long> tenantIds);

    /**
     * 根据系统配额ID全量查询其绑定的租户列表（查看某配额被哪些租户使用）
     * @param quotaId 系统配额ID（sQuotaId）
     * @return 该配额绑定的所有 QuotaTenantVO 列表，按创建时间倒序排列
     */
    List<QuotaTenantVO> getQuotaTenantsByQuotaId(Long quotaId);

    /**
     * 根据多个系统配额ID全量查询使用租户，并按配额ID分组返回结果
     * @param quotaIds 系统配额ID列表
     * @return 包含一个 Map 的列表，键为 sQuotaId，值为对应配额的 QuotaTenantVO 列表；输入为空时返回空列表
     */
    Map<Long, List<QuotaTenantVO>> getQuotaTenantsByQuotaIds(List<Long> quotaIds);

    /**
     * 根据系统租户ID查询其拥有的所有系统配额ID列表
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的系统配额ID去重集合（仅包含未软删记录）
     */
    Set<Long> getQuotaIdsByTenantId(Long tenantId);

    /**
     * 根据系统配额ID查询所有绑定该配额的系统租户ID列表
     * @param quotaId 系统配额ID（sQuotaId）
     * @return 绑定该配额的系统租户ID去重集合（仅包含未软删记录）
     */
    Set<Long> getTenantIdsByQuotaId(Long quotaId);

}
