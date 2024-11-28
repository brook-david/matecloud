package com.mate.system.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mate.core.auth.annotation.PreAuth;
import com.mate.core.cloud.util.CryptoUtil;
import com.mate.core.common.api.Result;
import com.mate.core.common.exception.BaseException;
import com.mate.core.database.entity.Search;
import com.mate.core.file.util.ExcelUtil;
import com.mate.core.log.annotation.Log;
import com.mate.core.web.controller.BaseController;
import com.mate.core.web.util.CollectionUtil;
import com.mate.system.entity.SysUser;
import com.mate.system.poi.SysUserPOI;
import com.mate.system.service.ISysUserService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;


/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author xuzf
 * @since 2020-06-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户管理")
public class SysUserController extends BaseController {

    private final ISysUserService sysUserService;

    private final PasswordEncoder passwordEncoder;

    /**
     * 用户列表
     *
     * @param search 　搜索关键词
     * @return Result
     */
    @PreAuth
    @Log(value = "用户列表", exception = "用户列表请求异常")
    @GetMapping("/page")
    @Operation(summary = "用户列表", description = "分页查询")
    @Parameters({
            @Parameter(name = "current", required = true,  description = "当前页", in = ParameterIn.DEFAULT),
            @Parameter(name = "size", required = true,  description = "每页显示数据", in = ParameterIn.DEFAULT),
            @Parameter(name = "keyword", required = true,  description = "模糊查询关键词", in = ParameterIn.DEFAULT),
            @Parameter(name = "startDate", required = true,  description = "创建开始日期", in = ParameterIn.DEFAULT),
            @Parameter(name = "endDate", required = true,  description = "创建结束日期", in = ParameterIn.DEFAULT),
            @Parameter(name = "prop", required = true,  description = "排序属性", in = ParameterIn.DEFAULT),
            @Parameter(name = "order", required = true,  description = "排序方式", in = ParameterIn.DEFAULT),
    })
    public Result<?> page(Search search, SysUser sysUser) {
        return Result.data(sysUserService.listPage(search, sysUser));
    }

    /**
     * 设置用户，支持新增或修改
     *
     * @param sysUser 用户信息
     * @return Result
     */
    @PreAuth
    @Log(value = "用户设置", exception = "设置用户请求异常")
    @PostMapping("/set")
    @Operation(summary = "设置用户", description = "新增或修改用户")
    public Result<?> set(@Valid @RequestBody SysUser sysUser) {
        String password = sysUser.getPassword();
        if (StringUtils.isNotBlank(password) && sysUser.getId() == null) {
            password = passwordEncoder.encode(CryptoUtil.encodeMD5(password));
            sysUser.setPassword(password);
        }
        return Result.condition(sysUserService.saveOrUpdate(sysUser));
    }

    /**
     * 用户信息
     *
     * @param id Id信息
     * @return Result
     */
    @PreAuth
    @Log(value = "用户信息", exception = "用户信息请求异常")
    @GetMapping("/get")
    @Operation(summary = "用户信息", description = "根据ID查询")
    @Parameters({
            @Parameter(name = "id", required = true,  description = "用户ID", in = ParameterIn.DEFAULT),
    })
    public Result<?> get(@RequestParam String id) {
        return Result.data(sysUserService.getById(id));
    }

    /**
     * 用户删除
     *
     * @param ids id字符串，根据,号分隔
     * @return Result
     */
    @PreAuth
    @Log(value = "用户删除", exception = "用户删除请求异常")
    @PostMapping("/del")
    @Operation(summary = "用户删除", description = "用户删除")
    @Parameters({
            @Parameter(name = "ids", required = true,  description = "多个用,号隔开", in = ParameterIn.DEFAULT)
    })
    public Result<?> del(@RequestParam String ids) {
        return Result.condition(sysUserService.removeByIds(CollectionUtil.stringToCollection(ids)));
    }

    /**
     * 设置用户状态
     *
     * @param ids    id字符串，根据,号分隔
     * @param status 状态标识，启用或禁用
     * @return Result
     */
    @PreAuth
    @Log(value = "用户状态", exception = "用户状态请求异常")
    @PostMapping("/set-status")
    @Operation(summary = "用户状态", description = "状态包括：启用、禁用")
    @Parameters({
            @Parameter(name = "ids", required = true,  description = "多个用,号隔开", in = ParameterIn.DEFAULT),
            @Parameter(name = "status", required = true,  description = "状态", in = ParameterIn.DEFAULT)
    })
    public Result<?> setStatus(@RequestParam String ids, @RequestParam String status) {
        return Result.condition(sysUserService.status(ids, status));
    }

    /**
     * 设置用户密码
     *
     * @param user 用户信息
     * @return Result
     */
    @PreAuth
    @Log(value = "用户密码设置", exception = "用户密码设置请求异常")
    @PostMapping("/set-password")
    @Operation(summary = "用户密码设置", description = "用户密码设置")
    @Parameters({
            @Parameter(name = "id", required = true,  description = "用户ID", in = ParameterIn.DEFAULT),
            @Parameter(name = "password", required = true,  description = "密码", in = ParameterIn.DEFAULT)
    })
    public Result<?> setPassword(@RequestBody SysUser user) {
        String pwd = null;
        if (StringUtils.isNotBlank(user.getPassword())) {
            pwd = passwordEncoder.encode(CryptoUtil.encodeMD5(user.getPassword()));
        }
        user.setPassword(pwd);
        if (user.getId() == null) {
            throw new BaseException("请求ID不能为空");
        }
        return Result.condition(sysUserService.updateById(user));
    }

    /**
     * 用户信息导出
     */
    @PreAuth
    @Log(value = "用户导出", exception = "导出用户请求异常")
    @PostMapping("/export")
    @Operation(summary = "导出用户", description = "导出用户")
    public void export(HttpServletResponse response) {
        List<SysUserPOI> sysUserPOIS = sysUserService.export();
        //使用工具类导出excel
        ExcelUtil.exportExcel(sysUserPOIS, null, "用户", SysUserPOI.class, "user", response);
    }
}

