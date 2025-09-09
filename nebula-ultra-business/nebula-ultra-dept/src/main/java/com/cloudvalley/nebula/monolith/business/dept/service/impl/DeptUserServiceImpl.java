package com.cloudvalley.nebula.monolith.business.dept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.converter.DeptUserConverter;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.DeptUser;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.DeptUserRTO;
import com.cloudvalley.nebula.monolith.business.dept.mapper.DeptUserMapper;
import com.cloudvalley.nebula.monolith.business.dept.service.IDeptUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.common.utils.FetchUtils;
import com.cloudvalley.nebula.monolith.common.utils.GeneratorUtils;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.DeptUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户用户-租户部门分配 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class DeptUserServiceImpl extends ServiceImpl<DeptUserMapper, DeptUser> implements IDeptUserService {

    @Autowired
    private DeptUserConverter deptUserConverter;

    /**
     * 分页查询所有有效的用户-部门关联记录（未删除），按创建时间倒序排列。
     *
     * @param page 分页参数对象
     * @return 包含 DeptUserVO 的分页结果
     */
    @Override
    public IPage<DeptUserVO> getDeptUserList(Page<DeptUser> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);

        // 使用 IService 的 page 方法执行分页查询
        IPage<DeptUser> userDeptPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(userDeptPage.getRecords());

        // 创建新的 IPage<DeptUserVO>，复用原分页参数
        return new Page<DeptUserVO>()
                .setRecords(voList)
                .setTotal(userDeptPage.getTotal())
                .setCurrent(userDeptPage.getCurrent())
                .setSize(userDeptPage.getSize());
    }

    /**
     * 根据多个 ID 分页查询用户-部门关联记录。
     *
     * @param ids  用户-部门关联 ID 列表
     * @param page 分页参数
     * @return 分页的 DeptUserVO 列表
     */
    @Override
    public IPage<DeptUserVO> getDeptUsersByIds(List<Long> ids, Page<DeptUser> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DeptUser::getId, ids)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);

        IPage<DeptUser> userDeptPage = this.page(page, queryWrapper);
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(userDeptPage.getRecords());

        return new Page<DeptUserVO>()
                .setRecords(voList)
                .setTotal(userDeptPage.getTotal())
                .setCurrent(userDeptPage.getCurrent())
                .setSize(userDeptPage.getSize());
    }

    /**
     * 根据用户 ID 分页查询其关联的部门列表。
     *
     * @param userId 用户 ID
     * @param page   分页参数
     * @return 分页的用户-部门 VO 列表
     */
    @Override
    public IPage<DeptUserVO> getDeptUsersByUserId(Long userId, Page<DeptUser> page) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTUserId, userId)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);

        IPage<DeptUser> userDeptPage = this.page(page, queryWrapper);
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(userDeptPage.getRecords());

        return new Page<DeptUserVO>()
                .setRecords(voList)
                .setTotal(userDeptPage.getTotal())
                .setCurrent(userDeptPage.getCurrent())
                .setSize(userDeptPage.getSize());
    }

    /**
     * 根据多个用户 ID 分页查询并按用户 ID 分组返回用户-部门关联数据。
     *
     * @param userIds 用户 ID 列表
     * @param page    分页参数
     * @return 分页的 Map<Long, List<DeptUserVO>>，键为用户 ID，值为该用户关联的部门 VO 列表
     */
    @Override
    public IPage<Map<Long, List<DeptUserVO>>> getDeptUsersByUserIds(List<Long> userIds, Page<DeptUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                userIds,
                // 分页参数
                page,
                // 查询条件：按用户ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(DeptUser::getTUserId, userIds)
                        .eq(DeptUser::getDeleted, false)
                        .orderByDesc(DeptUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                deptUserConverter::EnListToVOList,
                // 按用户ID分组
                DeptUserVO::getTUserId
        );
    }

    /**
     * 根据部门 ID 分页查询关联的用户列表。
     *
     * @param deptId 部门 ID
     * @param page   分页参数
     * @return 分页的用户-部门 VO 列表
     */
    @Override
    public IPage<DeptUserVO> getDeptUsersByDeptId(Long deptId, Page<DeptUser> page) {
        LambdaQueryWrapper<DeptUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeptUser::getTDeptId, deptId)
                .eq(DeptUser::getDeleted, false)
                .orderByDesc(DeptUser::getCreatedAt);

        IPage<DeptUser> userDeptPage = this.page(page, queryWrapper);
        List<DeptUserVO> voList = deptUserConverter.EnListToVOList(userDeptPage.getRecords());

        return new Page<DeptUserVO>()
                .setRecords(voList)
                .setTotal(userDeptPage.getTotal())
                .setCurrent(userDeptPage.getCurrent())
                .setSize(userDeptPage.getSize());
    }

    /**
     * 根据多个部门 ID 分页查询并按部门 ID 分组返回用户-部门关联数据。
     *
     * @param deptIds 部门 ID 列表
     * @param page    分页参数
     * @return 分页的 Map<Long, List<DeptUserVO>>，键为部门 ID，值为该部门下的用户 VO 列表
     */
    @Override
    public IPage<Map<Long, List<DeptUserVO>>> getDeptUsersByDeptIds(List<Long> deptIds, Page<DeptUser> page) {
        return FetchUtils.pageGroupQuery(
                // 要查询的ID列表
                deptIds,
                // 分页参数
                page,
                // 查询条件：按部门ID批量查询，且未删除，按创建时间倒序
                wrapper -> wrapper.in(DeptUser::getTDeptId, deptIds)
                        .eq(DeptUser::getDeleted, false)
                        .orderByDesc(DeptUser::getCreatedAt),
                // 执行分页查询（调用当前 Service 的 page 方法）
                this::page,
                // 实体列表转 VO 列表
                deptUserConverter::EnListToVOList,
                // 按部门ID分组
                DeptUserVO::getTDeptId
        );
    }

    /**
     * 创建新的用户-部门关联记录。
     *
     * @param userDeptRTO 请求传输对象，包含创建所需字段
     * @return 是否创建成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createDeptUser(DeptUserRTO userDeptRTO) {
        DeptUser userDept = new DeptUser();
        BeanUtils.copyProperties(userDeptRTO, userDept);

        // 设置默认值
        userDept.setId(GeneratorUtils.generateId());
        userDept.setCreatedAt(GeneratorUtils.generateCurrentTime());
        userDept.setUpdatedAt(GeneratorUtils.generateCurrentTime());
        userDept.setCreatedById(FetchUtils.getCurrentUserId());
        if (userDeptRTO.getState() == null && userDept.getDeleted() == null) {
            userDept.setState(true);
            userDept.setDeleted(false);
        }

        return this.save(userDept);
    }

    /**
     * 更新用户-部门关联记录。
     *
     * @param userDeptRTO 包含更新字段的 RTO 对象，必须包含 ID
     * @return 是否更新成功（记录存在且未删除）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptUser(DeptUserRTO userDeptRTO) {
        if (userDeptRTO.getId() == null) {
            return false;
        }

        DeptUser userDept = new DeptUser();
        BeanUtils.copyProperties(userDeptRTO, userDept);
        userDept.setUpdatedAt(GeneratorUtils.generateCurrentTime());

        LambdaUpdateWrapper<DeptUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptUser::getId, userDeptRTO.getId())
                .eq(DeptUser::getDeleted, false);

        return this.update(userDept, updateWrapper);
    }

    /**
     * 更新用户-部门关联的状态（启用/禁用）。
     *
     * @param id    用户-部门关联 ID
     * @param state 目标状态（true: 启用，false: 禁用）
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDeptUserState(Long id, Boolean state) {
        LambdaUpdateWrapper<DeptUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptUser::getId, id)
                .eq(DeptUser::getDeleted, false)
                .set(DeptUser::getState, state)
                .set(DeptUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 物理删除用户-部门关联记录（根据 ID）。
     *
     * @param id 记录 ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDeptUser(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除用户-部门关联记录（标记 deleted = true）。
     *
     * @param id 记录 ID
     * @return 是否软删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteDeptUser(Long id) {
        LambdaUpdateWrapper<DeptUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DeptUser::getId, id)
                .eq(DeptUser::getDeleted, false)
                .set(DeptUser::getDeleted, true)
                .set(DeptUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }

    /**
     * 批量物理删除多个用户-部门关联记录。
     *
     * @param ids ID 列表
     * @return 是否全部删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteDeptUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        return this.removeByIds(ids);
    }

    /**
     * 批量软删除多个用户-部门关联记录（标记 deleted = true）。
     *
     * @param ids ID 列表
     * @return 是否批量软删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteDeptUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<DeptUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DeptUser::getId, ids)
                .eq(DeptUser::getDeleted, false)
                .set(DeptUser::getDeleted, true)
                .set(DeptUser::getUpdatedAt, GeneratorUtils.generateCurrentTime());

        return this.update(updateWrapper);
    }
}
