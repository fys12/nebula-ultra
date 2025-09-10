package com.cloudvalley.nebula.ultra.business.user.controller;

import cn.dev33.satoken.util.SaResult;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudvalley.nebula.ultra.business.user.model.dto.LoginDTO;
import com.cloudvalley.nebula.ultra.business.user.model.vo.UserAggregatorInfoVO;
import com.cloudvalley.nebula.ultra.business.user.service.IUserAggregatorService;
import com.cloudvalley.nebula.ultra.core.auth.IdentityAuth;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckDeptVO;
import com.cloudvalley.nebula.ultra.core.model.vo.CheckRoleVO;
import com.cloudvalley.nebula.ultra.shared.api.user.model.vo.SysUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userAggregator")
public class UserAggregatorController {

    @Autowired
    private IUserAggregatorService iUserAggregatorService;

    @Autowired
    private IdentityAuth identityAuth;

    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 登录用户信息
     */
    @PostMapping("/login")
    public SaResult login(@RequestBody LoginDTO loginDTO) {
        SysUserVO user = iUserAggregatorService.login(loginDTO.getUsername(), loginDTO.getPasswordHash());
        if (user == null) {
            return SaResult.error("用户名或密码错误");
        }
        return SaResult.ok("登录成功").setData(user);
    }

    /**
     * 获取用户聚合信息 [ 用户 所属部门 角色 ] 分页
     * @param current 当前页码
     * @param size 每页大小
     * @return 用户聚合分页信息
     */
    @GetMapping("/{current}/{size}")
    public SaResult getUserAggregatorInfo(@PathVariable Integer current, @PathVariable Integer size) {
        IPage<UserAggregatorInfoVO> result = iUserAggregatorService.getUserAggregatorInfo(current, size);

        if (result.getRecords().isEmpty()) {
            return SaResult.ok("暂无系统用户数据").setData(new Page<>(current, size, 0));
        }
        return SaResult.ok("获取系统用户列表成功").setData(result);
    }

    @GetMapping("/text")
    public CheckRoleVO text() {
        return identityAuth.checkRole(8001L, List.of(1002L, 1003L));
    }

}
