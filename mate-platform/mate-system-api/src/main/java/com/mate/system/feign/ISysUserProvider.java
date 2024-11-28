package com.mate.system.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.ProviderConstant;
import com.mate.core.feign.constant.FeignConstant;
import com.mate.system.dto.UserInfo;
import com.mate.system.entity.SysUser;

/**
 * 用户调用类
 *
 * @author pangu
 */
@FeignClient(value = FeignConstant.MATE_CLOUD_SYSTEM)
public interface ISysUserProvider {

    /**
     * 根据id查询用户信息
     *
     * @param id id
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_ID)
    Result<SysUser> getUserById(@RequestParam("id") Long id);

    /**
     * 根据userName查询用户信息
     * @param userName　用户名
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_USERNAME)
    Result<UserInfo> getUserByUserName(@RequestParam("userName") String userName);

    /**
     * 根据手机号查询用户信息
     * @param mobile　手机号码
     * @return Result
     */
    @GetMapping(ProviderConstant.PROVIDER_USER_MOBILE)
    Result<UserInfo> getUserByMobile(@RequestParam("mobile") String mobile);

}
