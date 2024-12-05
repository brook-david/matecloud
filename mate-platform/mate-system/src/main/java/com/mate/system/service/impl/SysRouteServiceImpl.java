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
package com.mate.system.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.mate.core.database.entity.Search;
import com.mate.core.database.util.PageUtil;
import com.mate.system.entity.SysRoute;
import com.mate.system.mapper.SysRouteMapper;
import com.mate.system.service.ISysRouteService;
import com.mate.system.vo.SysRouteVO;

import java.util.List;

/**
 * <p>
 * 系统路由表 服务实现类
 * </p>
 *
 * @author pangu
 * @since 2020-10-17
 */
@Service
public class SysRouteServiceImpl extends ServiceImpl<SysRouteMapper, SysRoute> implements ISysRouteService {

	@Override
	public IPage<SysRoute> listPage(Search search) {
		LambdaQueryWrapper<SysRoute> queryWrapper = Wrappers.<SysRoute>query().lambda()
				.between(StrUtil.isNotBlank(search.getStartDate()), SysRoute::getCreateTime, search.getStartDate(), search.getEndDate());
		boolean isKeyword = StrUtil.isNotBlank(search.getKeyword());
		queryWrapper.like(isKeyword, SysRoute::getServiceId, search.getKeyword())
				.or(isKeyword)
				.like(isKeyword, SysRoute::getName, search.getKeyword());
		queryWrapper.orderByDesc(SysRoute::getCreateTime);
		return this.baseMapper.selectPage(PageUtil.getPage(search), queryWrapper);
	}

	@Override
	public List<SysRouteVO> listItem() {
		return this.baseMapper.listItem();
	}
}