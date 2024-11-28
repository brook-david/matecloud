package com.mate.code.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mate.code.entity.SysDataSource;
import com.mate.core.common.vo.BaseListVO;

import java.util.List;

/**
 * <p>
 * 数据源表 Mapper 接口
 * </p>
 *
 * @author xuzf
 * @since 2020-07-07
 */
public interface SysDataSourceMapper extends BaseMapper<SysDataSource> {

    List<BaseListVO> optionList();

}
