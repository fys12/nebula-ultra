package com.cloudvalley.nebula.ultra.business.combo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.combo.converter.ComboRoleConverter;
import com.cloudvalley.nebula.ultra.business.combo.model.entity.ComboRole;
import com.cloudvalley.nebula.ultra.business.combo.mapper.ComboRoleMapper;
import com.cloudvalley.nebula.ultra.shared.api.combo.service.IComboRoleCommonService;
import com.cloudvalley.nebula.ultra.shared.api.combo.model.vo.ComboRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComboRoleCommonServiceImpl extends ServiceImpl<ComboRoleMapper, ComboRole> implements IComboRoleCommonService {

    @Autowired
    private ComboRoleConverter comboRoleConverter;

    /**
     * 根据绑定ID查询单个套餐-角色绑定关系
     *
     * @param id 绑定关系的ID
     * @return 符合条件的套餐-角色绑定关系的VO对象，若不存在则返回null
     */
    @Override
    public ComboRoleVO getComboRoleById(Long id) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getId, id)
                .eq(ComboRole::getDeleted, false);
        ComboRole entity = this.getOne(queryWrapper);
        return entity != null ? comboRoleConverter.EnToVO(entity) : null;
    }

    /**
     * 根据绑定ID列表全量查询套餐-角色绑定关系
     *
     * @param ids 绑定关系的ID列表
     * @return 符合条件的套餐-角色绑定关系的VO列表，若ID列表为空则返回空列表
     */
    @Override
    public List<ComboRoleVO> getComboRolesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboRole::getId, ids)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        List<ComboRole> list = this.list(queryWrapper);
        return comboRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据系统套餐ID全量查询套餐-角色绑定关系
     *
     * @param sComboId 系统套餐的ID
     * @return 符合条件的套餐-角色绑定关系的VO列表
     */
    @Override
    public List<ComboRoleVO> getComboRolesBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSComboId, sComboId)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        List<ComboRole> list = this.list(queryWrapper);
        return comboRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据系统套餐ID列表全量查询套餐-角色绑定关系（返回分组结果）
     *
     * @param sComboIds 系统套餐的ID列表
     * @return 按系统套餐ID分组的套餐-角色绑定关系VO列表，若ID列表为空则返回空列表
     */
    @Override
    public List<Map<Long, List<ComboRoleVO>>> getComboRolesBySComboIds(List<Long> sComboIds) {
        if (sComboIds == null || sComboIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboRole::getSComboId, sComboIds)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        List<ComboRole> entities = this.list(queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(entities);
        Map<Long, List<ComboRoleVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboRoleVO::getSComboId));
        return Collections.singletonList(grouped);
    }

    /**
     * 根据系统角色ID全量查询套餐-角色绑定关系
     *
     * @param sRoleId 系统角色的ID
     * @return 符合条件的套餐-角色绑定关系的VO列表
     */
    @Override
    public List<ComboRoleVO> getComboRolesBySRoleId(Long sRoleId) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSRoleId, sRoleId)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        List<ComboRole> list = this.list(queryWrapper);
        return comboRoleConverter.EnListToVOList(list);
    }

    /**
     * 根据系统角色ID列表全量查询套餐-角色绑定关系（返回分组结果）
     *
     * @param sRoleIds 系统角色的ID列表
     * @return 按系统角色ID分组的套餐-角色绑定关系VO列表，若ID列表为空则返回空列表
     */
    @Override
    public List<Map<Long, List<ComboRoleVO>>> getComboRolesBySRoleIds(List<Long> sRoleIds) {
        if (sRoleIds == null || sRoleIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ComboRole::getSRoleId, sRoleIds)
                .eq(ComboRole::getDeleted, false)
                .orderByDesc(ComboRole::getCreatedAt);
        List<ComboRole> entities = this.list(queryWrapper);
        List<ComboRoleVO> voList = comboRoleConverter.EnListToVOList(entities);
        Map<Long, List<ComboRoleVO>> grouped = voList.stream().collect(Collectors.groupingBy(ComboRoleVO::getSRoleId));
        return Collections.singletonList(grouped);
    }

    /**
     * 根据系统套餐ID查询关联的系统角色ID列表
     *
     * @param sComboId 系统套餐的ID
     * @return 与该系统套餐关联的系统角色ID集合
     */
    @Override
    public Set<Long> getSRoleIdsBySComboId(Long sComboId) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSComboId, sComboId)
                .eq(ComboRole::getDeleted, false)
                .select(ComboRole::getSRoleId);
        List<ComboRole> list = this.list(queryWrapper);
        return list.stream().map(ComboRole::getSRoleId).collect(Collectors.toSet());
    }

    /**
     * 根据系统角色ID查询关联的系统套餐ID列表
     *
     * @param sRoleId 系统角色的ID
     * @return 与该系统角色关联的系统套餐ID集合
     */
    @Override
    public Set<Long> getSComboIdsBySRoleId(Long sRoleId) {
        LambdaQueryWrapper<ComboRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ComboRole::getSRoleId, sRoleId)
                .eq(ComboRole::getDeleted, false)
                .select(ComboRole::getSComboId);
        List<ComboRole> list = this.list(queryWrapper);
        return list.stream().map(ComboRole::getSComboId).collect(Collectors.toSet());
    }

}
