package com.cloudvalley.nebula.ultra.business.group.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermDetailsVO;
import com.cloudvalley.nebula.ultra.business.group.model.vo.GroupPermListVO;

public interface IGroupAggregatorService {

    /**
     * 获取 权限组 列表
     * @param current 当前页
     * @param size 每页数量
     * @return 权限组 列表
     */
    IPage<GroupPermListVO> getPermGroupInfo(Integer current, Integer size);

    /**
     * 获取 权限组 详细信息
     * @param groupId 权限组Id
     * @return 权限组 详细信息
     */
    GroupPermDetailsVO getPermGroupDetails(Long groupId);

}
