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
package com.mate.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.mate.code.entity.Column;
import com.mate.code.service.IColumnService;
import com.mate.core.auth.annotation.PreAuth;
import com.mate.core.common.api.Result;
import com.mate.core.database.entity.Search;
import com.mate.core.log.annotation.Log;
import com.mate.core.web.controller.BaseController;
import com.mate.core.web.util.CollectionUtil;

import javax.validation.Valid;

/**
 * <p>
 * 代码生成字段表 前端控制器
 * </p>
 *
 * @author pangu
 * @since 2022-03-21
 */
@RestController
@AllArgsConstructor
@RequestMapping("/column")
@Tag(name = "代码生成字段表", description = "代码生成字段表接口")
public class ColumnController extends BaseController {

    private final IColumnService columnService;

    /**
     * 分页列表
     *
     * @param search 　搜索关键词
     * @return Result
     */
    @PreAuth
    @Log(value = "代码生成字段表列表", exception = "代码生成字段表列表请求异常")
    @GetMapping("/page")
    @Operation(summary = "代码生成字段表列表", description = "分页查询")
    @Parameters({
        @Parameter(name = "current", required = true,  description = "当前页", in = ParameterIn.DEFAULT),
        @Parameter(name = "size", required = true,  description = "每页显示数据", in = ParameterIn.DEFAULT),
        @Parameter(name = "keyword", required = true,  description = "模糊查询关键词", in = ParameterIn.DEFAULT),
        @Parameter(name = "startDate", required = true,  description = "创建开始日期", in = ParameterIn.DEFAULT),
        @Parameter(name = "endDate", required = true,  description = "创建结束日期", in = ParameterIn.DEFAULT),
    })
    public Result<?> page(Search search) {
		return Result.data(columnService.listPage(search));
    }

    /**
     * 代码生成字段表信息
     *
     * @param id Id
     * @return Result
     */
    @PreAuth
    @Log(value = "代码生成字段表信息", exception = "代码生成字段表信息请求异常")
    @GetMapping("/get")
    @Operation(summary = "代码生成字段表信息", description = "根据ID查询")
    @Parameters({
            @Parameter(name = "id", required = true,  description = "ID", in = ParameterIn.DEFAULT),
    })
    public Result<?> get(@RequestParam String id) {
		return Result.data(columnService.getById(id));
	}

    /**
    * 代码生成字段表设置
    *
    * @param column Column 对象
    * @return Result
    */
    @PreAuth
    @Log(value = "代码生成字段表设置", exception = "代码生成字段表设置请求异常")
    @PostMapping("/set")
    @Operation(summary = "代码生成字段表设置", description = "代码生成字段表设置,支持新增或修改")
    public Result<?> set(@Valid @RequestBody Column column) {
		return Result.condition(columnService.saveOrUpdate(column));
	}

    /**
    * 代码生成字段表删除
    *
    * @param ids id字符串，根据,号分隔
    * @return Result
    */
    @PreAuth
    @Log(value = "代码生成字段表删除", exception = "代码生成字段表删除请求异常")
    @PostMapping("/del")
    @Operation(summary = "代码生成字段表删除", description = "代码生成字段表删除")
    @Parameters({
            @Parameter(name = "ids", required = true,  description = "多个用,号隔开", in = ParameterIn.DEFAULT)
    })
    public Result<?> del(@RequestParam String ids) {
		return Result.condition(columnService.removeByIds(CollectionUtil.stringToCollection(ids)));
	}
}

