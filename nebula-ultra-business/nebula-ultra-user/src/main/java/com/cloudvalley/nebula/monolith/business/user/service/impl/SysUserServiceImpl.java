package com.cloudvalley.nebula.monolith.business.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.user.converter.SysUserConverter;
import com.cloudvalley.nebula.monolith.business.user.model.entity.SysUser;
import com.cloudvalley.nebula.monolith.business.user.model.rto.SysUserRTO;
import com.cloudvalley.nebula.monolith.business.user.mapper.SysUserMapper;
import com.cloudvalley.nebula.monolith.business.user.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudvalley.nebula.monolith.shared.api.user.model.vo.SysUserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserConverter sysUserConverter;

    /**
     * 查询系统用户列表 [分页]
     * @param page 分页参数
     * @return 系统用户VO的分页列表
     */
    @Override
    public IPage<SysUserVO> getUserList(Page<SysUser> page) {
        // 构建查询条件，按创建时间倒序排列
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getDeleted, false)
                .orderByDesc(SysUser::getCreatedAt);

        // 使用父类 ServiceImpl 的 page 方法执行分页查询
        IPage<SysUser> userPage = this.page(page, queryWrapper);

        // 转换 VO 列表
        List<SysUserVO> voList = sysUserConverter.EnListToVOList(userPage.getRecords());

        // 创建新的 IPage<SysUserVO>，复用原分页参数
        return new Page<SysUserVO>()
                .setRecords(voList)
                .setTotal(userPage.getTotal())
                .setCurrent(userPage.getCurrent())
                .setSize(userPage.getSize());
    }

    /**
     * 根据ID批量查询系统用户 [分页]
     * @param ids 用户ID列表
     * @param page 分页参数
     * @return 分页的用户VO列表
     */
    @Override
    public IPage<SysUserVO> getUsersByIds(List<Long> ids, Page<SysUser> page) {
        if (ids == null || ids.isEmpty()) {
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId, ids)
                .eq(SysUser::getDeleted, false)
                .orderByDesc(SysUser::getCreatedAt);

        IPage<SysUser> userPage = this.page(page, queryWrapper);
        List<SysUserVO> voList = sysUserConverter.EnListToVOList(userPage.getRecords());

        return new Page<SysUserVO>()
                .setRecords(voList)
                .setTotal(userPage.getTotal())
                .setCurrent(userPage.getCurrent())
                .setSize(userPage.getSize());
    }

    /**
     * 根据用户名 和 密码 查询系统用户
     * @param username 用户名
     * @param passwordHash 密码(hash加密后)
     * @return 用户信息
     */
    @Override
    public SysUserVO getUserByUsernameAndPasswordHash(String username, String passwordHash) {
        if (username == null || passwordHash == null) {
            return null;
        }

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername, username)
                .eq(SysUser::getPasswordHash, passwordHash)
                .eq(SysUser::getDeleted, false);

        SysUser user = this.getOne(queryWrapper);
        return user != null ? sysUserConverter.EnToVO(user) : null;
    }

    /**
     * 新增系统用户
     * @param userRTO 用户信息传输对象
     * @return 是否创建成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser(SysUserRTO userRTO) {
        SysUser user = new SysUser();
        BeanUtils.copyProperties(userRTO, user);

        // 设置默认值
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setState(true);
        user.setDeleted(false);

        // 使用父类 save 方法
        return this.save(user);
    }

    /**
     * 更新系统用户
     * @param userRTO 用户信息（需包含ID）
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUserRTO userRTO) {
        if (userRTO.getId() == null) {
            return false;
        }

        SysUser user = new SysUser();
        BeanUtils.copyProperties(userRTO, user);
        user.setUpdatedAt(LocalDateTime.now());

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId, userRTO.getId())
                .eq(SysUser::getDeleted, false);

        // 使用 update(entity, wrapper)
        return this.update(user, updateWrapper);
    }

    /**
     * 更新系统用户状态
     * @param id    用户ID
     * @param state 目标状态（true: 启用, false: 禁用）
     * @return 是否更新成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserState(Long id, Boolean state) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId, id)
                .eq(SysUser::getDeleted, false)
                .set(SysUser::getState, state)
                .set(SysUser::getUpdatedAt, LocalDateTime.now());

        // update(null, wrapper) 表示只更新指定字段
        return this.update(null, updateWrapper);
    }

    /**
     * 删除系统用户（物理删除）
     * @param id 用户ID
     * @return 是否删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUser(Long id) {
        return this.removeById(id);
    }

    /**
     * 软删除系统用户（标记 deleted = true）
     * @param id 用户ID
     * @return 是否软删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean softDeleteUser(Long id) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysUser::getId, id)
                .eq(SysUser::getDeleted, false)
                .set(SysUser::getDeleted, true)
                .set(SysUser::getUpdatedAt, LocalDateTime.now());

        return this.update(null, updateWrapper);
    }

    /**
     * 批量删除系统用户（物理删除）
     * @param ids 用户ID列表
     * @return 是否全部删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }
        return this.removeByIds(ids);
    }

    /**
     * 批量软删除系统用户
     * @param ids 用户ID列表
     * @return 是否批量软删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSoftDeleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return false;
        }

        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysUser::getId, ids)
                .eq(SysUser::getDeleted, false)
                .set(SysUser::getDeleted, true)
                .set(SysUser::getUpdatedAt, LocalDateTime.now());

        return this.update(null, updateWrapper);
    }

}
