package com.cloudvalley.nebula.monolith.shared.api.tenant.service;

import com.cloudvalley.nebula.monolith.shared.api.tenant.model.vo.TenantSubscribeVO;

import java.util.List;
import java.util.Map;

public interface ITenantSubscribeCommonService {

    /**
     * 根据租户订阅ID查询单个订阅信息
     * @param id 订阅记录的唯一标识ID
     * @return 对应的 TenantSubscribeVO 对象，若不存在或已软删则返回 null
     */
    TenantSubscribeVO getTenantSubscribeById(Long id);

    /**
     * 根据多个订阅ID全量查询订阅信息（不分页）
     * @param ids 订阅记录ID列表
     * @return 所有匹配的 TenantSubscribeVO 列表，仅包含未软删记录；输入为空时返回空列表
     */
    List<TenantSubscribeVO> getTenantSubscribesByIds(List<Long> ids);

    /**
     * 根据租户ID全量查询其订阅的套餐列表（查看某租户的所有订阅）
     * @param tenantId 租户ID（sTenantId）
     * @return 该租户绑定的所有 TenantSubscribeVO 列表，按创建时间倒序排列
     */
    List<TenantSubscribeVO> getTenantSubscribesByTenantId(Long tenantId);

    /**
     * 根据多个租户ID全量查询订阅信息，并按租户ID分组返回结果
     * @param tenantIds 租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 TenantSubscribeVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByTenantIds(List<Long> tenantIds);

    /**
     * 根据套餐ID全量查询其被订阅情况（查看某套餐被哪些租户订阅）
     * @param comboId 套餐ID（sComboId）
     * @return 订阅了该套餐的所有 TenantSubscribeVO 列表，按创建时间倒序排列
     */
    List<TenantSubscribeVO> getTenantSubscribesByComboId(Long comboId);

    /**
     * 根据多个套餐ID全量查询订阅租户，并按套餐ID分组返回结果
     * @param comboIds 套餐ID列表
     * @return 包含一个 Map 的列表，键为 sComboId，值为对应套餐的 TenantSubscribeVO 列表；输入为空时返回空列表
     */
    List<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByComboIds(List<Long> comboIds);

}
