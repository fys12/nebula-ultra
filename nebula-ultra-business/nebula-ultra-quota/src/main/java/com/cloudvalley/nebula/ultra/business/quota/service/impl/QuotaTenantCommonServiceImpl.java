package com.cloudvalley.nebula.ultra.business.quota.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.quota.converter.QuotaTenantConverter;
import com.cloudvalley.nebula.ultra.business.quota.mapper.QuotaTenantMapper;
import com.cloudvalley.nebula.ultra.business.quota.model.entity.QuotaTenant;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.QuotaTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.service.IQuotaTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuotaTenantCommonServiceImpl extends ServiceImpl<QuotaTenantMapper, QuotaTenant> implements IQuotaTenantCommonService {

    @Autowired
    private QuotaTenantConverter quotaTenantConverter;

    /**
     * 根据租户配额ID查询单个绑定信息
     * @param id 租户配额关联的唯一标识ID
     * @return 对应的 QuotaTenantVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public QuotaTenantVO getQuotaTenantById(Long id) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getId, id)
                .eq(QuotaTenant::getDeleted, false);

        QuotaTenant quotaTenant = this.getOne(queryWrapper);
        return quotaTenant != null ? quotaTenantConverter.EnToVO(quotaTenant) : null;
    }

    /**
     * 根据多个租户配额ID全量查询绑定信息（不分页）
     * @param ids 租户配额关联ID列表
     * @return 所有匹配的 QuotaTenantVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<QuotaTenantVO> getQuotaTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaTenant::getId, ids)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);
        List<QuotaTenant> list = this.list(queryWrapper);
        return quotaTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据系统租户ID全量查询其绑定的配额列表（查看某租户拥有哪些系统配额）
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的所有 QuotaTenantVO 列表，按创建时间倒序排列
     */
    @Override
    public List<QuotaTenantVO> getQuotaTenantsByTenantId(Long tenantId) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSTenantId, tenantId)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);
        List<QuotaTenant> list = this.list(queryWrapper);
        return quotaTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统租户ID全量查询绑定配额，并按租户ID分组返回结果
     * @param tenantIds 系统租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 QuotaTenantVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaTenant::getSTenantId, tenantIds)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);
        List<QuotaTenant> entities = this.list(queryWrapper);
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(entities);
        Map<Long, List<QuotaTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaTenantVO::getSTenantId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据系统配额ID全量查询其绑定的租户列表（查看某配额被哪些租户使用）
     * @param quotaId 系统配额ID（sQuotaId）
     * @return 该配额绑定的所有 QuotaTenantVO 列表，按创建时间倒序排列
     */
    @Override
    public List<QuotaTenantVO> getQuotaTenantsByQuotaId(Long quotaId) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSQuotaId, quotaId)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);
        List<QuotaTenant> list = this.list(queryWrapper);
        return quotaTenantConverter.EnListToVOList(list);
    }

    /**
     * 根据多个系统配额ID全量查询使用租户，并按配额ID分组返回结果
     * @param quotaIds 系统配额ID列表
     * @return 包含一个 Map 的列表，键为 sQuotaId，值为对应配额的 QuotaTenantVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<QuotaTenantVO>>> getQuotaTenantsByQuotaIds(List<Long> quotaIds) {
        if (quotaIds == null || quotaIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(QuotaTenant::getSQuotaId, quotaIds)
                .eq(QuotaTenant::getDeleted, false)
                .orderByDesc(QuotaTenant::getCreatedAt);
        List<QuotaTenant> entities = this.list(queryWrapper);
        List<QuotaTenantVO> voList = quotaTenantConverter.EnListToVOList(entities);
        Map<Long, List<QuotaTenantVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(QuotaTenantVO::getSQuotaId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据系统租户ID查询其拥有的所有系统配额ID列表
     * @param tenantId 系统租户ID（sTenantId）
     * @return 该租户绑定的系统配额ID去重集合（仅包含未软删记录）
     */
    @Override
    public Set<Long> getQuotaIdsByTenantId(Long tenantId) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSTenantId, tenantId)
                .eq(QuotaTenant::getDeleted, false)
                .select(QuotaTenant::getSQuotaId);

        List<QuotaTenant> quotaTenants = this.list(queryWrapper);
        return quotaTenants.stream()
                .map(QuotaTenant::getSQuotaId)
                .collect(Collectors.toSet());
    }

    /**
     * 根据系统配额ID查询所有绑定该配额的系统租户ID列表
     * @param quotaId 系统配额ID（sQuotaId）
     * @return 绑定该配额的系统租户ID去重集合（仅包含未软删记录）
     */
    @Override
    public Set<Long> getTenantIdsByQuotaId(Long quotaId) {
        LambdaQueryWrapper<QuotaTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuotaTenant::getSQuotaId, quotaId)
                .eq(QuotaTenant::getDeleted, false)
                .select(QuotaTenant::getSTenantId);

        List<QuotaTenant> quotaTenants = this.list(queryWrapper);
        return quotaTenants.stream()
                .map(QuotaTenant::getSTenantId)
                .collect(Collectors.toSet());
    }

}
