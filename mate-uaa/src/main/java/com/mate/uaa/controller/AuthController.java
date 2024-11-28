package com.mate.uaa.controller;

import com.xkcoding.justauth.AuthRequestFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;
import com.mate.core.auth.annotation.PreAuth;
import com.mate.core.common.api.Result;
import com.mate.core.common.constant.Oauth2Constant;
import com.mate.core.common.entity.LoginUser;
import com.mate.core.common.util.SecurityUtil;
import com.mate.core.common.util.StringPool;
import com.mate.core.common.util.StringUtil;
import com.mate.core.log.annotation.Log;
import com.mate.core.redis.core.RedisService;
import com.mate.system.dto.UserInfo;
import com.mate.system.feign.ISysRolePermissionProvider;
import com.mate.system.feign.ISysUserProvider;
import com.mate.uaa.config.SocialConfig;
import com.mate.uaa.enums.LoginType;
import com.mate.uaa.service.ValidateService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 认证控制类
 *
 * @author pangu
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
@Tag(name = "认证管理")
public class AuthController {

    @Qualifier("consumerTokenServices")
    private final ConsumerTokenServices consumerTokenServices;

    private final ValidateService validateService;

    private final ISysUserProvider sysUserProvider;

    private final ISysRolePermissionProvider sysRolePermissionProvider;

    private final AuthRequestFactory factory;

    private final SocialConfig socialConfig;

    private final RedisService redisService;

    @Log(value = "用户信息", exception = "用户信息请求异常")
    @GetMapping("/get/user")
    @Parameters({
            @Parameter(name = "Mate-Auth", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    @Operation(summary = "用户信息", description = "用户信息")
    public Result<?> getUser(HttpServletRequest request) {

        LoginUser loginUser = SecurityUtil.getUsername(request);
        UserInfo userInfo = null;
        /**
         * 根据type来判断调用哪个接口登录，待扩展社交登录模式
         * type 1:用户名和密码登录　2：手机号码登录
         */
        if (loginUser.getType() == LoginType.MOBILE.getType()) {
            userInfo = sysUserProvider.getUserByMobile(loginUser.getAccount()).getData();
        } else {
            userInfo = sysUserProvider.getUserByUserName(loginUser.getAccount()).getData();
        }

        Map<String, Object> data = new HashMap<>(7);
        data.put("username", loginUser.getAccount());
        data.put("avatar", userInfo.getSysUser().getAvatar());
        data.put("roleId", userInfo.getSysUser().getRoleId());
        data.put("departId", userInfo.getSysUser().getDepartId());
        data.put("tenantId", userInfo.getSysUser().getTenantId());
        data.put("realName", userInfo.getSysUser().getRealName());
        data.put("nickName", userInfo.getSysUser().getName());
        List<String> stringList = sysRolePermissionProvider.getMenuIdByRoleId(String.valueOf(userInfo.getSysUser().getRoleId()));
        data.put("permissions", stringList);
        // 存入redis,以用于mate-starter-auth的PreAuthAspect查询权限使用
        redisService.set(Oauth2Constant.MATE_PERMISSION_PREFIX + loginUser.getAccount()
                + StringPool.DOT + userInfo.getSysUser().getRoleId(), data);
        return Result.data(data);
    }

    @Log(value = "验证码获取", exception = "验证码获取请求异常")
    @GetMapping("/code")
    @Operation(summary = "验证码获取", description = "验证码获取")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    public Result<?> authCode() {
        return validateService.getCode();
    }

    @Log(value = "退出登录", exception = "退出登录请求异常")
    @PostMapping("/logout")
    @Operation(summary = "退出登录", description = "退出登录")
    @Parameters({
            @Parameter(name = "Mate-Auth", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    public Result<?> logout(HttpServletRequest request) {
        if (StringUtil.isNotBlank(SecurityUtil.getHeaderToken(request))) {
            consumerTokenServices.revokeToken(SecurityUtil.getToken(request));
        }
        return Result.success("操作成功");
    }

    /**
     * 验证码下发
     *
     * @param mobile 手机号码
     * @return Result
     */
    @Log(value = "手机验证码下发", exception = "手机验证码下发请求异常")
    @Operation(summary = "手机验证码下发", description = "手机验证码下发")
    @Parameters({
            @Parameter(name = "Authorization", required = true,  description = "授权类型", in = ParameterIn.QUERY)
    })
    @GetMapping("/sms-code")
    public Result<?> smsCode(String mobile) {
        return validateService.getSmsCode(mobile);
    }


    /**
     * 登录类型
     */
    @Log(value = "登录类型", exception = "登录类型请求异常")
    @GetMapping("/list")
    @Operation(summary = "登录类型", description = "登录类型")
    public Map<String, String> loginType() {
        List<String> oauthList = factory.oauthList();
        return oauthList.stream().collect(Collectors.toMap(oauth -> oauth.toLowerCase() + "登录", oauth -> "http://localhost:10001/mate-uaa/auth/login/" + oauth.toLowerCase()));
    }

    /**
     * 登录
     *
     * @param oauthType 第三方登录类型
     * @param response  response
     * @throws IOException IO异常
     */
    @Log(value = "第三方登录", exception = "第三方登录请求异常")
    @Operation(summary = "第三方登录", description = "第三方登录")
    @PostMapping("/login/{oauthType}")
    public void login(@PathVariable String oauthType, HttpServletResponse response) throws IOException {
        AuthRequest authRequest = factory.get(oauthType);
        response.sendRedirect(authRequest.authorize(oauthType + "::" + AuthStateUtils.createState()));
    }

    /**
     * 登录成功后的回调
     *
     * @param oauthType 第三方登录类型
     * @param callback  携带返回的信息
     */
    @Log(value = "第三方登录回调", exception = "第三方登录回调请求异常")
    @Operation(summary = "第三方登录回调", description = "第三方登录回调")
    @GetMapping("/callback/{oauthType}")
    public void callback(@PathVariable String oauthType, AuthCallback callback, HttpServletResponse httpServletResponse) throws IOException {
        String url = socialConfig.getUrl() + "?code=" + oauthType + "-" + callback.getCode() + "&state=" + callback.getState();
        log.debug("url:{}", url);
        //跳转到指定页面
        httpServletResponse.sendRedirect(url);
    }

    @PreAuth
    @Log(value = "用户按钮权限")
    @GetMapping("/get/permission")
    @Operation(summary = "用户按钮权限")
    public Result<?> getPermission(HttpServletRequest request) {
        LoginUser loginUser = SecurityUtil.getUsername(request);
        List<String> stringList = sysRolePermissionProvider.getMenuIdByRoleId(loginUser.getRoleId());
        return Result.data(stringList);
    }
}
