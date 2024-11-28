package com.mate.system.feign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.ProviderConstant;
import com.mate.core.log.annotation.Log;
import com.mate.system.entity.SysDict;
import com.mate.system.service.ISysDictService;

import java.util.List;

/**
 * 字典远程调用
 * @author pangu
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "字典远程调用")
public class SysDictProvider implements ISysDictProvider {

    private final ISysDictService sysDictService;

    @Override
    @Operation(summary = "字典值", description = "根据code和dictKey获取值")
    @Log(value = "字典值", exception = "字典值请求失败")
    @GetMapping(ProviderConstant.PROVIDER_DICT_VALUE)
    public Result<String> getValue(String code, String dictKey) {
        return sysDictService.getValue(code, dictKey);
    }

    @Override
    @Operation(summary = "字典列表", description = "根据code获取字典列表")
    @Log(value = "字典列表", exception = "字典列表请求失败")
    @GetMapping(ProviderConstant.PROVIDER_DICT_LIST)
    public Result<List<SysDict>> getList(String code) {
        return sysDictService.getList(code);
    }
}
