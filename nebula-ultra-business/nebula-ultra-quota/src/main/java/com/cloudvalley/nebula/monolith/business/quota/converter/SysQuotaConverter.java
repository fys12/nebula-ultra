package com.cloudvalley.nebula.monolith.business.quota.converter;

import com.cloudvalley.nebula.monolith.business.quota.model.entity.SysQuota;
import com.cloudvalley.nebula.monolith.shared.api.quoat.model.vo.SysQuotaVO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SysQuotaConverter {

    /**
     * 将 SysQuota 实体转换为 SysQuotaVO 视图对象
     * @param sysQuota 源实体对象
     * @return 转换后的 VO 对象
     */
    SysQuotaVO EnToVO(SysQuota sysQuota);

    /**
     * 将 SysQuota 实体列表批量转换为 VO 列表
     * @param sysQuotaList 实体对象列表
     * @return 转换后的 VO 对象列表，复用 EnToVO 的映射规则
     */
    List<SysQuotaVO> EnListToVOList(List<SysQuota> sysQuotaList);

}
