package com.mate.system.feign;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.ProviderConstant;
import com.mate.core.common.dto.CommonLog;
import com.mate.core.log.feign.ISysLogProvider;
import com.mate.system.entity.SysLog;
import com.mate.system.service.ISysLogService;

/**
 * 日志远程调用
 * @author pangu
 */
@Slf4j
@RestController
@AllArgsConstructor
@Tag(name = "日志远程调用")
public class SysLogProvider implements ISysLogProvider {

    private final ISysLogService sysLogService;

    @Override
    @PostMapping(ProviderConstant.PROVIDER_LOG_SET)
    @Operation(summary = "日志设置", description = "日志设置")
    public Result<Boolean> set(CommonLog commonLog) {
        SysLog sysLog = new SysLog();
        BeanUtils.copyProperties(commonLog, sysLog);
        return Result.data(sysLogService.save(sysLog));
    }
}
