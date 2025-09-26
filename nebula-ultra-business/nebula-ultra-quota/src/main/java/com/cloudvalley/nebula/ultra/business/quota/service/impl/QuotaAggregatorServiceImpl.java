package com.cloudvalley.nebula.ultra.business.quota.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.quota.model.vo.QuoatDetailsVO;
import com.cloudvalley.nebula.ultra.business.quota.service.IQuotaAggregatorService;
import com.cloudvalley.nebula.ultra.business.quota.service.IQuotaTenantService;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.QuotaTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.service.ISysQuotaCommonService;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.service.ISysTenantCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuotaAggregatorServiceImpl implements IQuotaAggregatorService {

    @Autowired
    private IQuotaTenantService iQuotaTenantService;

    @Autowired
    private ISysQuotaCommonService iSysQuotaCommonService;

    @Autowired
    private ISysTenantCommonService iSysTenantCommonService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    /**
     * 获取租户 配额 信息
     * @param current 当前页
     * @param size 每页数量
     * @return 租户配额信息
     */
    @Override
    public IPage<QuoatDetailsVO> getTenantQuotaInfo(Integer current, Integer size) {
        // 1. 查询 租户配额 基本信息（分页）
        IPage<QuotaTenantVO> quotaTenantList = iQuotaTenantService.getQuotaTenantList(new Page<>(current, size));

        // 2. 获取 租户Id 和 配额Id 列表
        List<Long> tenantIds = quotaTenantList.getRecords().stream()
                .map(QuotaTenantVO::getSTenantId)
                .filter(id -> id != null)
                .toList();

        List<Long> quotaIds = quotaTenantList.getRecords().stream()
                .map(QuotaTenantVO::getSQuotaId)
                .filter(id -> id != null)
                .toList();

        // 3. 根据 租户Id 查询 租户信息
        Map<Long, SysTenantVO> tenantMap = iSysTenantCommonService.getSysTenantsByIds(tenantIds);

        // 4. 根据 配额Id 获取 配额信息
        List<SysQuotaVO> sysQuotaList = iSysQuotaCommonService.getSysQuotasByIds(quotaIds);
        Map<Long, SysQuotaVO> quotaMap = sysQuotaList.stream()
                .collect(Collectors.toMap(SysQuotaVO::getId, quota -> quota));

        // 5. 获取 创建人Id 列表
        List<Long> userIds = sysQuotaList.stream()
                .map(SysQuotaVO::getCreatedById)
                .filter(id -> id != null)
                .toList();

        // 6. 根据 创建人Id 查询 创建人信息
        Map<Long, SysUserVO> userMap = iSysUserCommonService.getUsersByIds(userIds);

        // 7. 组装数据
        List<QuoatDetailsVO> detailsList = quotaTenantList.getRecords().stream()
                .map(quotaTenant -> {
                    // 获取租户信息
                    SysTenantVO tenant = tenantMap.get(quotaTenant.getSTenantId());

                    // 获取配额信息
                    SysQuotaVO quota = quotaMap.get(quotaTenant.getSQuotaId());

                    // 获取创建人信息
                    SysUserVO createdByUser = null;
                    if (quota != null) {
                        createdByUser = userMap.get(quota.getCreatedById());
                    }

                    // 构建 QuoatDetailsVO 对象
                    if (quota != null) {
                        return new QuoatDetailsVO(
                                quota.getId(),
                                quota.getCode(),
                                quota.getName(),
                                quota.getDesc(),
                                quota.getPrice(),
                                quota.getUnit(),
                                quota.getColor(),
                                quota.getState(),
                                quota.getCreatedAt(),
                                quota.getUpdatedAt(),
                                createdByUser,
                                quota.getDeleted(),
                                tenant
                        );
                    }
                    return null;
                })
                .filter(quotaDetails -> quotaDetails != null)
                .toList();

        // 8. 创建新的分页对象并返回
        IPage<QuoatDetailsVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(detailsList);
        resultPage.setTotal(quotaTenantList.getTotal());
        resultPage.setPages(quotaTenantList.getPages());

        return resultPage;
    }
}
