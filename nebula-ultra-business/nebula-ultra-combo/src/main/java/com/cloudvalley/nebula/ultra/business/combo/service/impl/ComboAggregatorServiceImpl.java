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
     * 获取租 套餐 详情信息
     * 包含 套餐 包含的 权限、角色、配额
     * @param current 当前页
     * @param size 每页数量
     * @return 套餐详情信息
     */
    @Override
    public IPage<ComboDetailsVO> getComboInfo(Integer current, Integer size) {
        // 1. 查询 套餐 基本信息
        IPage<SysComboVO> sysComboList = iSysComboService.getSysComboList(new Page<>(current, size));

        // 1.2 获取 套餐Id 列表
        List<Long> comboIds = sysComboList.getRecords().stream()
                .map(SysComboVO::getId)
                .filter(id -> id != null)
                .toList();

        // 1.3 获取 套餐 创建人Id
        List<Long> userIds = sysComboList.getRecords().stream()
                .map(SysComboVO::getCreatedById)
                .filter(id -> id != null)
                .toList();

        // 2. 根据 套餐Id 查询 套餐包含的权限
        Map<Long, List<ComboPermVO>> comboPermsMap = iComboPermCommonService.getComboPermsBySComboIds(comboIds);

        // 2.1 获取权限ID列表，用于查询权限详情
        Set<Long> permIds = comboPermsMap.values().stream()
                .flatMap(List::stream)
                .map(ComboPermVO::getSPermId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 2.2 根据权限ID查询权限详情
        Map<Long, SysPermVO> permMap;
        if (!permIds.isEmpty()) {
            List<SysPermVO> permList = iSysPermCommonService.getSysPermsByIds(new ArrayList<>(permIds));
            permMap = permList.stream().collect(Collectors.toMap(SysPermVO::getId, perm -> perm));
        } else {
            permMap = Collections.emptyMap();
        }

        // 3. 根据 套餐Id 查询 套餐包含的配额
        Map<Long, List<ComboQuotaVO>> comboQuotasMap = iComboQuotaCommonService.getComboQuotasBySComboIds(comboIds);

        // 3.1 获取配额ID列表，用于查询配额详情
        Set<Long> quotaIds = comboQuotasMap.values().stream()
                .flatMap(List::stream)
                .map(ComboQuotaVO::getSQuotaId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 3.2 根据配额ID查询配额详情
        Map<Long, SysQuotaVO> quotaMap;
        if (!quotaIds.isEmpty()) {
            List<SysQuotaVO> quotaList = iSysQuotaCommonService.getSysQuotasByIds(new ArrayList<>(quotaIds));
            quotaMap = quotaList.stream().collect(Collectors.toMap(SysQuotaVO::getId, quota -> quota));
        } else {
            quotaMap = Collections.emptyMap();
        }

        // 4. 根据 套餐Id 查询 套餐包含的角色
        Map<Long, List<ComboRoleVO>> comboRolesMap = iComboRoleCommonService.getComboRolesBySComboIds(comboIds);

        // 4.1 获取角色ID列表，用于查询角色详情
        Set<Long> roleIds = comboRolesMap.values().stream()
                .flatMap(List::stream)
                .map(ComboRoleVO::getSRoleId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // 4.2 根据角色ID查询角色详情
        Map<Long, SysRoleVO> roleMap;
        if (!roleIds.isEmpty()) {
            roleMap = iSysRoleCommonService.getSysRolesByIds(new ArrayList<>(roleIds));
        } else {
            roleMap = Collections.emptyMap();
        }

        // 5. 根据 创建人Id 查询 创建人信息
        Map<Long, SysUserVO> userMap = iSysUserCommonService.getUsersByIds(userIds);

        // 6. 组装数据
        List<ComboDetailsVO> detailsList = sysComboList.getRecords().stream()
                .map(combo -> {
                    // 获取权限列表
                    List<SysPermVO> perms = comboPermsMap.getOrDefault(combo.getId(), List.of()).stream()
                            .map(perm -> permMap.get(perm.getSPermId()))
                            .filter(Objects::nonNull)
                            .toList();

                    // 获取配额列表
                    List<SysQuotaVO> quotas = comboQuotasMap.getOrDefault(combo.getId(), List.of()).stream()
                            .map(quota -> quotaMap.get(quota.getSQuotaId()))
                            .filter(Objects::nonNull)
                            .toList();

                    // 获取角色列表
                    List<SysRoleVO> roles = comboRolesMap.getOrDefault(combo.getId(), List.of()).stream()
                            .map(role -> roleMap.get(role.getSRoleId()))
                            .filter(Objects::nonNull)
                            .toList();

                    // 获取创建人信息
                    SysUserVO createdByUser = userMap.get(combo.getCreatedById());

                    return new ComboDetailsVO(
                            combo.getId(),
                            combo.getName(),
                            combo.getDesc(),
                            combo.getPrice(),
                            combo.getCreatedAt(),
                            combo.getUpdatedAt(),
                            createdByUser,
                            combo.getColor(),
                            combo.getDeleted(),
                            perms,
                            roles,
                            quotas
                    );
                })
                .toList();

        // 7. 创建新的分页对象并返回
        IPage<ComboDetailsVO> resultPage = new Page<>(current, size);
        resultPage.setRecords(detailsList);
        resultPage.setTotal(sysComboList.getTotal());
        resultPage.setPages(sysComboList.getPages());

        return resultPage;
    }
}
