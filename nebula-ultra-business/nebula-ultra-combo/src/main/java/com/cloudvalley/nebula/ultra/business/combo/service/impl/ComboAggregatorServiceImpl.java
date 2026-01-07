package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.combo.model.vo.ComboDetailsVO;
import com.cloudvalley.nebula.ultra.business.combo.service.*;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboPermVO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboQuotaVO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.SysComboVO;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.IComboPermCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.IComboQuotaCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.IComboRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.ISysComboCommonService;
import com.cloudvalley.nebula.ultra.shared.api.perm.model.vo.SysPermVO;
import com.cloudvalley.nebula.ultra.shared.api.perm.service.ISysPermCommonService;
import com.cloudvalley.nebula.ultra.shared.api.quoat.model.vo.SysQuotaVO;
import com.cloudvalley.nebula.ultra.shared.api.quoat.service.ISysQuotaCommonService;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import com.cloudvalley.nebula.ultra.shared.api.user.service.ISysUserCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ComboAggregatorServiceImpl implements IComboAggregatorService {

    @Autowired
    private ISysComboService iSysComboService;

    @Autowired
    private ISysComboCommonService iSysComboCommonService;

    @Autowired
    private IComboPermCommonService iComboPermCommonService;

    @Autowired
    private IComboQuotaCommonService iComboQuotaCommonService;

    @Autowired
    private IComboRoleCommonService iComboRoleCommonService;

    @Autowired
    private ISysPermCommonService iSysPermCommonService;

    @Autowired
    private ISysQuotaCommonService iSysQuotaCommonService;

    @Autowired
    private ISysRoleCommonService iSysRoleCommonService;

    @Autowired
    private ISysUserCommonService iSysUserCommonService;

    /**
     * 获取 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param comboId 套餐Id
     * @return 套餐详情信息
     */
    @Override
    public ComboDetailsVO getComboDetails(Long comboId) {
        // 1. 查询 套餐 基本信息
        SysComboVO sysComboById = iSysComboCommonService.getSysComboById(comboId);

        // 2. 根据 套餐Id 查询 套餐包含的权限
        List<ComboPermVO> comboPermsBySComboId = iComboPermCommonService.getComboPermsBySComboId(comboId);

        // 2.1 获取权限ID列表，用于查询权限详情
        List<Long> permIds = comboPermsBySComboId.stream()
                .map(ComboPermVO::getSPermId)
                .distinct()
                .toList();

        // 2.2 根据权限ID查询权限详情
        Map<Long, SysPermVO> permMap;
        if (!permIds.isEmpty()) {
            List<SysPermVO> permList = iSysPermCommonService.getSysPermsByIds(permIds);
            permMap = permList.stream()
                    .collect(Collectors.toMap(SysPermVO::getId, perm -> perm));
        } else {
            permMap = Collections.emptyMap();
        }

        // 3. 根据 套餐Id 查询 套餐包含的配额
        List<ComboQuotaVO> comboQuotasBySComboId = iComboQuotaCommonService.getComboQuotasBySComboId(comboId);

        // 3.1 获取配额ID列表，用于查询配额详情
        List<Long> quotaIds = comboQuotasBySComboId.stream()
                .map(ComboQuotaVO::getSQuotaId)
                .distinct()
                .toList();

        // 3.2 建立配额ID与套餐配额的映射关系
        Map<Long, ComboQuotaVO> comboQuotaMap = comboQuotasBySComboId.stream()
                .collect(Collectors.toMap(ComboQuotaVO::getSQuotaId, comboQuota -> comboQuota));

        // 3.3 根据配额ID查询配额详情
        Map<Long, SysQuotaVO> quotaMap;
        if (!quotaIds.isEmpty()) {
            List<SysQuotaVO> quotaList = iSysQuotaCommonService.getSysQuotasByIds(quotaIds);
            quotaMap = quotaList.stream()
                    .collect(Collectors.toMap(SysQuotaVO::getId, quota -> quota));
        } else {
            quotaMap = Collections.emptyMap();
        }

        // 4. 根据 套餐Id 查询 套餐包含的角色
        List<ComboRoleVO> comboRolesBySComboId = iComboRoleCommonService.getComboRolesBySComboId(comboId);

        // 4.1 获取角色ID列表，用于查询角色详情
        List<Long> roleIds = comboRolesBySComboId.stream()
                .map(ComboRoleVO::getSRoleId)
                .distinct()
                .toList();

        // 4.2 根据角色ID查询角色详情
        Map<Long, SysRoleVO> roleMap;
        if (!roleIds.isEmpty()) {
            List<SysRoleVO> roleList = iSysRoleCommonService.getSysRolesByIds(roleIds);
            roleMap = roleList.stream()
                    .collect(Collectors.toMap(SysRoleVO::getId, role -> role));
        } else {
            roleMap = Collections.emptyMap();
        }

        // 5. 根据 创建人Id 查询 创建人信息
        SysUserVO userById = iSysUserCommonService.getUserById(sysComboById.getCreatedById());

        // 6. 组装配额详情列表，包含配额基本信息和可用量
        List<ComboDetailsVO.QuotaDetail> quotaDetails = comboQuotasBySComboId.stream()
                .map(comboQuota -> {
                    Long quotaId = comboQuota.getSQuotaId();
                    SysQuotaVO sysQuota = quotaMap.get(quotaId);
                    if (sysQuota != null) {
                        ComboDetailsVO.QuotaDetail quotaDetail = new ComboDetailsVO.QuotaDetail();
                        quotaDetail.setId(sysQuota.getId());
                        quotaDetail.setName(sysQuota.getName());
                        quotaDetail.setCode(sysQuota.getCode());
                        quotaDetail.setDesc(sysQuota.getDesc());
                        quotaDetail.setPrice(sysQuota.getPrice());
                        quotaDetail.setUnit(sysQuota.getUnit());
                        quotaDetail.setColor(sysQuota.getColor());
                        quotaDetail.setValue(comboQuota.getValue()); // 设置配额可用量
                        return quotaDetail;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        // 7. 组装数据
        ComboDetailsVO comboDetailsVO = new ComboDetailsVO(
                sysComboById.getId(),
                sysComboById.getName(),
                sysComboById.getDesc(),
                sysComboById.getPrice(),
                sysComboById.getCreatedAt(),
                sysComboById.getUpdatedAt(),
                userById,
                sysComboById.getColor(),
                sysComboById.getDeleted(),
                permMap.values().stream().toList(),
                roleMap.values().stream().toList(),
                quotaDetails
        );

        return comboDetailsVO;
    }
}
