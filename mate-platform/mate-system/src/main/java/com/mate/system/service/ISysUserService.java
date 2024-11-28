package com.mate.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mate.core.database.entity.Search;
import com.mate.system.entity.SysUser;
import com.mate.system.poi.SysUserPOI;

import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author xuzf
 * @since 2020-06-18
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 用户状态设置
     *
     * @param ids    id列表串
     * @param status 状态
     * @return boolean
     */
    boolean status(String ids, String status);

    /**
     * 业务分页
     *
     * @param search  分页搜索
     * @param sysUser 部门参数
     * @return 分页列表
     */
    IPage<SysUser> listPage(Search search, SysUser sysUser);


    /**
     * 忽略租户查询用户信息
     *
     * @param sysUser 用户对象
     * @return 用户对象
     */
    SysUser getOneIgnoreTenant(SysUser sysUser);

    /**
     * 导出列表
     *
     * @return 导出POI数据
     */
    List<SysUserPOI> export();

}
