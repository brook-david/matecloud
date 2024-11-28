/*
 * Copyright 2020-2030, MateCloud, DAOTIANDI Technology Inc All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Author: pangu(7333791@qq.com)
 */
package com.mate.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import com.mate.core.auth.annotation.PreAuth;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.MateConstant;
import com.mate.core.database.entity.Search;
import com.mate.core.log.annotation.Log;
import com.mate.core.redis.core.RedisService;
import com.mate.core.web.controller.BaseController;
import com.mate.core.web.util.CollectionUtil;
import com.mate.system.entity.SysApi;
import com.mate.system.service.ISysApiService;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * API管理 前端控制器
 * </p>
 *
 * @author pangu
 * @since 2020-10-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name  = "API管理")
public class SysApiController extends BaseController {

    private final ISysApiService sysApiService;

    private final RedisService redisService;

    /**
     * 分页列表
     *
     * @param page   分页信息
     * @param search 　搜索关键词
     * @return Result
     */
    @PreAuth
    @Log(value = "API分页")
    @GetMapping("/page")
    @Operation(summary = "API分页")
    @Parameters({
            @Parameter(name = "current", required = true,  description = "当前页", in = ParameterIn.DEFAULT),
            @Parameter(name = "size", required = true,  description = "每页显示数据", in = ParameterIn.DEFAULT),
            @Parameter(name = "keyword", required = true,  description = "模糊查询关键词", in = ParameterIn.DEFAULT),
            @Parameter(name = "startDate", required = true,  description = "创建开始日期", in = ParameterIn.DEFAULT),
            @Parameter(name = "endDate", required = true,  description = "创建结束日期", in = ParameterIn.DEFAULT),
    })
    public Result<?> page(Page<?> page, Search search, @Nullable @RequestParam String serviceId) {
        return Result.data(sysApiService.listPage(page, search, serviceId));
    }

    /**
     * API管理信息
     *
     * @param id Id
     * @return Result
     */
    @PreAuth
    @Log(value = "API信息")
    @GetMapping("/get")
    @Operation(summary = "API信息")
    @Parameters({
            @Parameter(name = "id", required = true,  description = "ID", in = ParameterIn.DEFAULT),
    })
    public Result<?> get(@RequestParam String id) {
        return Result.data(sysApiService.getById(id));
    }

    /**
     * API管理设置
     *
     * @param sysApi SysApi 对象
     * @return Result
     */
    @PreAuth
    @Log(value = "API设置")
    @PostMapping("/set")
    @Operation(summary = "API设置")
    public Result<?> set(@Valid @RequestBody SysApi sysApi) {
        return Result.condition(sysApiService.saveOrUpdate(sysApi));
    }

    /**
     * API管理删除
     *
     * @param ids id字符串，根据,号分隔
     * @return Result
     */
    @PreAuth
    @Log(value = "API删除")
    @PostMapping("/del")
    @Operation(summary = "API删除")
    @Parameters({
            @Parameter(name = "ids", required = true,  description = "多个用,号隔开", in = ParameterIn.DEFAULT)
    })
    public Result<?> del(@RequestParam String ids) {
        return Result.condition(sysApiService.removeByIds(CollectionUtil.stringToCollection(ids)));
    }

    /**
     * API状态
     *
     * @param ids    多个Id，用,号分隔
     * @param status 状态：启用、禁用
     * @return Result
     */
    @PreAuth
    @Log(value = "API状态")
    @PostMapping("/set-status")
    @Operation(summary = "API状态")
    @Parameters({
            @Parameter(name = "ids", required = true,  description = "多个id用,号隔开", in = ParameterIn.DEFAULT),
            @Parameter(name = "status", required = true,  description = "状态", in = ParameterIn.DEFAULT)
    })
    public Result<?> setStatus(@RequestParam String ids, @RequestParam String status) {
        return Result.condition(sysApiService.status(ids, status));
    }

    /**
     * 从redis同步api至数据库
     *
     * @return Boolean
     */
    @PreAuth
    @PostMapping("/sync")
    @Operation(summary = "API同步")
    @Log(value = "API同步")
    public Result<?> sync() {
        Set<Object> serviceIds = redisService.sGet(MateConstant.MATE_SERVICE_RESOURCE);
        for (Object service : serviceIds) {
            if (redisService.hHasKey(MateConstant.MATE_API_RESOURCE, service.toString())) {
                Map<String, Object> apiMap = (Map<String, Object>) redisService.hget(MateConstant.MATE_API_RESOURCE, service.toString());
                List<Map<String, String>> list = (List<Map<String, String>>) apiMap.get("list");
                list.forEach(item -> {
                    SysApi sysApi = new SysApi();
                    sysApi.setAuth(item.get("auth"));
                    sysApi.setClassName(item.get("className"));
                    sysApi.setCode(item.get("code"));
                    sysApi.setContentType(item.get("contentType"));
                    sysApi.setMethod(item.get("method"));
                    sysApi.setMethodName(item.get("methodName"));
                    sysApi.setName(item.get("name"));
                    sysApi.setNotes(item.get("notes"));
                    sysApi.setPath(item.get("path"));
                    sysApi.setServiceId(item.get("serviceId"));
                    SysApi exist = sysApiService.getByCode(sysApi.getCode());
                    if (ObjectUtils.isEmpty(exist)) {
                        sysApiService.save(sysApi);
                    } else {
                        LambdaQueryWrapper<SysApi> queryWrapper = new LambdaQueryWrapper<>();
                        queryWrapper.eq(SysApi::getCode, sysApi.getCode());
                        sysApiService.update(sysApi, queryWrapper);
                    }
                });
            }
            redisService.hdel(MateConstant.MATE_API_RESOURCE, service.toString());
        }
        redisService.del(MateConstant.MATE_SERVICE_RESOURCE);
        return Result.condition(true);
    }
}

