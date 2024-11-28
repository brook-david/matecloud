package com.mate.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.ProviderConstant;
import com.mate.core.feign.constant.FeignConstant;
import com.mate.system.entity.SysDict;

import java.util.List;

/**
 * 字典远程调用接口类
 * @author pangu
 */
@FeignClient(value = FeignConstant.MATE_CLOUD_SYSTEM)
public interface ISysDictProvider {

    /**
     * 根据code和dictKey获取值
     * @param code code
     * @param dictKey key
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_DICT_VALUE)
    Result<String> getValue(@RequestParam("code") String code, @RequestParam("dictKey") String dictKey);

    /**
     * 根据code获取字典列表
     * @param code　code
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_DICT_LIST)
    Result<List<SysDict>> getList(@RequestParam("code") String code);

}
