package com.cloudvalley.nebula.ultra.business.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.role.converter.SysRoleConverter;
import com.cloudvalley.nebula.ultra.business.role.mapper.SysRoleMapper;
import com.cloudvalley.nebula.ultra.business.role.model.entity.SysRole;
import com.cloudvalley.nebula.ultra.shared.api.role.model.vo.SysRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.role.service.ISysRoleCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysRoleCommonServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleCommonService {

    @Autowired
    private SysRoleConverter sysRoleConverter;

    /**
     * 根据ID查询单个系统角色信息
     * @param id 系统角色唯一标识ID
     * @return 对应的 SysRoleVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public SysRoleVO getSysRoleById(Long id) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRole::getId, id)
                .eq(SysRole::getDeleted, false);

        SysRole sysRole = this.getOne(queryWrapper);
        return sysRole != null ? sysRoleConverter.EnToVO(sysRole) : null;
    }

    /**
     * 根据多个系统角色ID全量查询角色信息（不分页）
     * @param ids 系统角色ID列表
     * @return 所有匹配的 SysRoleVO 列表 Map<角色Id, 角色VO>
     */
    @Override
    public Map<Long, SysRoleVO> getSysRolesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRole::getId, ids)
                .eq(SysRole::getDeleted, false)
                .orderByDesc(SysRole::getCreatedAt);

        List<SysRole> list = this.list(queryWrapper);
        List<SysRoleVO> voList = sysRoleConverter.EnListToVOList(list);

        return voList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        SysRoleVO::getId,
                        vo -> vo
                ));
    }

}
