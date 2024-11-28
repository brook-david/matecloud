package com.mate.code.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mate.code.entity.SysDataSource;
import com.mate.core.common.vo.BaseListVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据源表 服务类
 * </p>
 *
 * @author xuzf
 * @since 2020-07-07
 */
public interface ISysDataSourceService extends IService<SysDataSource> {

    IPage<SysDataSource> listPage(Map<String, String> query);

    List<BaseListVO> optionList();
}
