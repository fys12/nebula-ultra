package com.cloudvalley.nebula.monolith.business.dept.controller;


import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.monolith.business.dept.model.entity.DeptUser;
import com.cloudvalley.nebula.monolith.business.dept.model.rto.DeptUserRTO;
import com.cloudvalley.nebula.monolith.business.dept.service.IDeptUserService;
import com.cloudvalley.nebula.monolith.shared.api.dept.model.vo.DeptUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租户用户-租户部门分配 前端控制器
 * </p>
 *
 * @author shy
 * @since 2025-08-26
 */
@RestController
@RequestMapping("/dept-user")
public class DeptUserController {

    @Autowired
    private IDeptUserService userDeptService;

    /**
     * 查询租户用户-租户部门分配列表 [分页]
     * @param current 当前页码，默认值为1（表示查询第一页数据）
     * @param size 每页条数，默认值为10（表示每页展示10条数据）
     * @return SaResult 接口响应结果：成功时返回分页的用户部门分配VO列表；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping
    public SaResult getDeptUserList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<DeptUserVO> result = userDeptService.getDeptUserList(page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户部门分配数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户部门分配列表成功").setData(result);
    }

    /**
     * 根据租户用户-租户部门分配ID列表批量查询分配信息 [分页]
     * @param ids 租户用户-租户部门分配关系的主键ID列表（雪花算法ID集合，可传入单个或多个ID）
     * @param current 当前页码，默认值为1
     * @param size 每页条数，默认值为10
     * @return SaResult 接口响应结果：成功时返回分页的用户部门分配VO列表；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping("/batch")
    public SaResult getDeptUsersByIds(@RequestParam List<Long> ids,
                                      @RequestParam(value = "current", defaultValue = "1") Integer current,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<DeptUserVO> result = userDeptService.getDeptUsersByIds(ids, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无用户部门分配数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户部门分配信息成功").setData(result);
    }

    /**
     * 根据租户用户ID查询该用户的部门分配列表 [分页，查单个用户所属部门]
     * @param userId 租户用户的主键ID（雪花算法ID）
     * @param current 当前页码，默认值为1
     * @param size 每页条数，默认值为10
     * @return SaResult 接口响应结果：成功时返回分页的用户部门分配VO列表；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping("/by-user/{userId}")
    public SaResult getDeptUsersByUserId(@PathVariable Long userId,
                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<DeptUserVO> result = userDeptService.getDeptUsersByUserId(userId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该用户暂未分配任何部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取用户部门分配列表成功").setData(result);
    }

    /**
     * 根据租户用户ID列表批量查询用户的部门分配列表 [分页，查多个用户各自所属部门]
     * @param userIds 租户用户的主键ID列表（雪花算法ID集合）
     * @param current 当前页码，默认值为1
     * @param size 每页条数，默认值为10
     * @return SaResult 接口响应结果：成功时返回分页的分组数据（key：用户ID，value：该用户的部门分配VO列表）；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping("/by-users")
    public SaResult getDeptUsersByUserIds(@RequestParam List<Long> userIds,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<Map<Long, List<DeptUserVO>>> result = userDeptService.getDeptUsersByUserIds(userIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些用户暂未分配任何部门").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取用户部门分配列表成功").setData(result);
    }

    /**
     * 根据租户部门ID查询该部门的用户分配列表 [分页，查单个部门下的用户]
     * @param deptId 租户部门的主键ID（雪花算法ID）
     * @param current 当前页码，默认值为1
     * @param size 每页条数，默认值为10
     * @return SaResult 接口响应结果：成功时返回分页的用户部门分配VO列表；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping("/by-dept/{deptId}")
    public SaResult getDeptUsersByDeptId(@PathVariable Long deptId,
                                         @RequestParam(value = "current", defaultValue = "1") Integer current,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<DeptUserVO> result = userDeptService.getDeptUsersByDeptId(deptId, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("该部门下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取部门用户分配列表成功").setData(result);
    }

    /**
     * 根据租户部门ID列表批量查询部门的用户分配列表 [分页，查多个部门各自下的用户]
     * @param deptIds 租户部门的主键ID列表（雪花算法ID集合）
     * @param current 当前页码，默认值为1
     * @param size 每页条数，默认值为10
     * @return SaResult 接口响应结果：成功时返回分页的分组数据（key：部门ID，value：该部门的用户分配VO列表）；无数据时返回空分页结构，均包含对应提示信息
     */
    @GetMapping("/by-depts")
    public SaResult getDeptUsersByDeptIds(@RequestParam List<Long> deptIds,
                                          @RequestParam(value = "current", defaultValue = "1") Integer current,
                                          @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Page<DeptUser> page = new Page<>(current, size);
        IPage<Map<Long, List<DeptUserVO>>> result = userDeptService.getDeptUsersByDeptIds(deptIds, page);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("这些部门下暂无用户").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("批量获取部门用户分配列表成功").setData(result);
    }

    /**
     * 新增租户用户-租户部门分配关系
     * @param userDeptRTO 新增用户部门分配的请求参数对象（包含用户ID、部门ID等核心业务字段）
     * @return SaResult 接口响应结果：新增成功返回成功提示；新增失败返回错误提示
     */
    @PostMapping
    public SaResult createDeptUser(@RequestBody DeptUserRTO userDeptRTO) {
        boolean result = userDeptService.createDeptUser(userDeptRTO);
        if (result) {
            return SaResult.ok("创建用户部门分配成功");
        }
        return SaResult.error("创建用户部门分配失败");
    }

    /**
     * 更新租户用户-租户部门分配关系
     * @param id 待更新的用户部门分配关系主键ID（雪花算法ID，从路径参数获取）
     * @param userDeptRTO 更新用户部门分配的请求参数对象（需包含更新后的业务字段，主键ID会被路径参数覆盖）
     * @return SaResult 接口响应结果：更新成功返回成功提示；更新失败返回错误提示
     */
    @PutMapping("/{id}")
    public SaResult updateDeptUser(@PathVariable Long id, @RequestBody DeptUserRTO userDeptRTO) {
        userDeptRTO.setId(id);
        boolean result = userDeptService.updateDeptUser(userDeptRTO);
        if (result) {
            return SaResult.ok("更新用户部门分配成功");
        }
        return SaResult.error("更新用户部门分配失败");
    }

    /**
     * 更新租户用户在某个部门中的分配状态（启用/禁用）
     * @param id 待更新状态的用户部门分配关系主键ID（雪花算法ID）
     * @param state 目标状态（true：启用，false：禁用）
     * @return SaResult 接口响应结果：状态更新成功返回成功提示；更新失败返回错误提示
     */
    @PatchMapping("/{id}/state")
    public SaResult updateDeptUserState(@PathVariable Long id, @RequestParam Boolean state) {
        boolean result = userDeptService.updateDeptUserState(id, state);
        if (result) {
            return SaResult.ok("更新用户部门状态成功");
        }
        return SaResult.error("更新用户部门状态失败");
    }

    /**
     * 物理删除租户用户-租户部门分配关系（直接删除数据库记录，不可恢复）
     * @param id 待删除的用户部门分配关系主键ID（雪花算法ID）
     * @return SaResult 接口响应结果：删除成功返回成功提示；删除失败返回错误提示
     */
    @DeleteMapping("/{id}")
    public SaResult deleteDeptUser(@PathVariable Long id) {
        boolean result = userDeptService.deleteDeptUser(id);
        if (result) {
            return SaResult.ok("删除用户部门分配成功");
        }
        return SaResult.error("删除用户部门分配失败");
    }

    /**
     * 软删除租户用户-租户部门分配关系（仅更新deleted字段为true，保留数据库记录，可追溯）
     * @param id 待软删除的用户部门分配关系主键ID（雪花算法ID）
     * @return SaResult 接口响应结果：软删除成功返回成功提示；软删除失败返回错误提示
     */
    @PatchMapping("/{id}/soft-delete")
    public SaResult softDeleteDeptUser(@PathVariable Long id) {
        boolean result = userDeptService.softDeleteDeptUser(id);
        if (result) {
            return SaResult.ok("软删除用户部门分配成功");
        }
        return SaResult.error("软删除用户部门分配失败");
    }

    /**
     * 批量物理删除租户用户-租户部门分配关系（直接删除数据库记录，不可恢复）
     * @param ids 待批量删除的用户部门分配关系主键ID列表（雪花算法ID集合）
     * @return SaResult 接口响应结果：批量删除成功返回成功提示；批量删除失败返回错误提示
     */
    @DeleteMapping("/batch")
    public SaResult batchDeleteDeptUsers(@RequestParam List<Long> ids) {
        boolean result = userDeptService.batchDeleteDeptUsers(ids);
        if (result) {
            return SaResult.ok("批量删除用户部门分配成功");
        }
        return SaResult.error("批量删除用户部门分配失败");
    }

}
