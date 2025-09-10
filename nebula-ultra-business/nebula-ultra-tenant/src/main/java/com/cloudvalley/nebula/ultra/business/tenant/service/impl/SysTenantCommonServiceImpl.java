package com.cloudvalley.nebula.ultra.business.tenant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.ultra.business.tenant.converter.SysTenantConverter;
import com.cloudvalley.nebula.ultra.business.tenant.mapper.SysTenantMapper;
import com.cloudvalley.nebula.ultra.business.tenant.model.entity.SysTenant;
import com.cloudvalley.nebula.ultra.shared.api.tenant.model.vo.SysTenantVO;
import com.cloudvalley.nebula.ultra.shared.api.tenant.service.ISysTenantCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysTenantCommonServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantCommonService {

    @Autowired
    private SysTenantConverter sysTenantConverter;

    /**
     * 根据ID查询单个系统租户信息
     * @param id 系统租户唯一标识ID
     * @return 对应的 SysTenantVO 对象，若不存在或已软删则返回 null
     */
    @Override
    public SysTenantVO getSysTenantById(Long id) {
        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysTenant::getId, id)
                .eq(SysTenant::getDeleted, false);

        SysTenant sysTenant = this.getOne(queryWrapper);
        return sysTenant != null ? sysTenantConverter.EnToVO(sysTenant) : null;
    }

    /**
     * 根据多个系统租户ID全量查询租户信息（不分页）
     * @param ids 系统租户ID列表
     * @return 所有匹配的 SysTenantVO Map<租户Id 租户VO>
     */
    @Override
    public Map<Long, SysTenantVO> getSysTenantsByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyMap();
        }

        LambdaQueryWrapper<SysTenant> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysTenant::getId, ids)
                .eq(SysTenant::getDeleted, false)
                .orderByDesc(SysTenant::getCreatedAt);

        List<SysTenant> list = this.list(queryWrapper);
        List<SysTenantVO> voList = sysTenantConverter.EnListToVOList(list);

        return voList.stream()
                .collect(java.util.stream.Collectors.toMap(
                        SysTenantVO::getId,
                        vo -> vo
                ));
    }

}
