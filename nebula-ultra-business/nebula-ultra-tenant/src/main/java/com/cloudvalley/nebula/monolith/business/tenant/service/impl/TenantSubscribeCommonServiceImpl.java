package com.cloudvalley.nebula.monolith.business.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.business.tenant.converter.TenantSubscribeConverter;
import com.cloudvalley.nebula.monolith.business.tenant.mapper.TenantSubscribeMapper;
import com.cloudvalley.nebula.monolith.business.tenant.model.entity.TenantSubscribe;
import com.cloudvalley.nebula.monolith.shared.api.tenant.model.vo.TenantSubscribeVO;
import com.cloudvalley.nebula.monolith.shared.api.tenant.service.ITenantSubscribeCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TenantSubscribeCommonServiceImpl extends ServiceImpl<TenantSubscribeMapper, TenantSubscribe> implements ITenantSubscribeCommonService {

    @Autowired
    private TenantSubscribeConverter tenantSubscribeConverter;

    /**
     * 根据租户订阅ID查询单个订阅信息
     * @param id 订阅记录的唯一标识ID
     * @return 对应的 TenantSubscribeVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public TenantSubscribeVO getTenantSubscribeById(Long id) {
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getId, id)
                .eq(TenantSubscribe::getDeleted, false);

        TenantSubscribe tenantSubscribe = this.getOne(queryWrapper);
        return tenantSubscribe != null ? tenantSubscribeConverter.EnToVO(tenantSubscribe) : null;
    }

    /**
     * 根据多个订阅ID全量查询订阅信息（不分页）
     * @param ids 订阅记录ID列表
     * @return 所有匹配的 TenantSubscribeVO 列表，按创建时间倒序排列；输入为空时返回空列表
     */
    @Override
    public List<TenantSubscribeVO> getTenantSubscribesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TenantSubscribe::getId, ids)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);
        List<TenantSubscribe> list = this.list(queryWrapper);
        return tenantSubscribeConverter.EnListToVOList(list);
    }

    /**
     * 根据租户ID全量查询其订阅的套餐列表
     * @param tenantId 租户ID（sTenantId）
     * @return 该租户绑定的所有 TenantSubscribeVO 列表，按创建时间倒序排列
     */
    @Override
    public List<TenantSubscribeVO> getTenantSubscribesByTenantId(Long tenantId) {
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getSTenantId, tenantId)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);
        List<TenantSubscribe> list = this.list(queryWrapper);
        return tenantSubscribeConverter.EnListToVOList(list);
    }

    /**
     * 根据多个租户ID全量查询订阅信息，并按租户ID分组返回结果
     * @param tenantIds 租户ID列表
     * @return 包含一个 Map 的列表，键为 sTenantId，值为对应租户的 TenantSubscribeVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByTenantIds(List<Long> tenantIds) {
        if (tenantIds == null || tenantIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TenantSubscribe::getSTenantId, tenantIds)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);
        List<TenantSubscribe> entities = this.list(queryWrapper);
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(entities);
        Map<Long, List<TenantSubscribeVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(TenantSubscribeVO::getSTenantId));
        return java.util.Collections.singletonList(grouped);
    }

    /**
     * 根据套餐ID全量查询其被订阅情况
     * @param comboId 套餐ID（sComboId）
     * @return 订阅了该套餐的所有 TenantSubscribeVO 列表，按创建时间倒序排列
     */
    @Override
    public List<TenantSubscribeVO> getTenantSubscribesByComboId(Long comboId) {
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TenantSubscribe::getSComboId, comboId)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);
        List<TenantSubscribe> list = this.list(queryWrapper);
        return tenantSubscribeConverter.EnListToVOList(list);
    }

    /**
     * 根据多个套餐ID全量查询订阅租户，并按套餐ID分组返回结果
     * @param comboIds 套餐ID列表
     * @return 包含一个 Map 的列表，键为 sComboId，值为对应套餐的 TenantSubscribeVO 列表；输入为空时返回空列表
     */
    @Override
    public List<Map<Long, List<TenantSubscribeVO>>> getTenantSubscribesByComboIds(List<Long> comboIds) {
        if (comboIds == null || comboIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        LambdaQueryWrapper<TenantSubscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TenantSubscribe::getSComboId, comboIds)
                .eq(TenantSubscribe::getDeleted, false)
                .orderByDesc(TenantSubscribe::getCreatedAt);
        List<TenantSubscribe> entities = this.list(queryWrapper);
        List<TenantSubscribeVO> voList = tenantSubscribeConverter.EnListToVOList(entities);
        Map<Long, List<TenantSubscribeVO>> grouped = voList.stream().collect(java.util.stream.Collectors.groupingBy(TenantSubscribeVO::getSComboId));
        return java.util.Collections.singletonList(grouped);
    }

}
