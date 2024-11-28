package com.mate.system.strategy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.mate.core.auth.util.MateAuthUser;
import com.mate.core.common.exception.BaseException;
import com.mate.core.database.enums.DataScopeTypeEnum;
import com.mate.system.dto.RoleDTO;
import com.mate.system.service.ISysDepartService;

import java.util.List;

/**
 * 本级及以下级别的数据权限
 *
 * @author pangu
 */
@Component("3")
@AllArgsConstructor
public class ThisLevelChildrenDataScope implements AbstractDataScopeHandler {

	private final ISysDepartService sysDepartService;

	@Override
	public List<Long> getDeptIds(RoleDTO roleDto, DataScopeTypeEnum dataScopeTypeEnum) {
		String deptId = MateAuthUser.getUser().getDeptId();
		if (deptId == null) {
			throw new BaseException("部门信息为空！");
		}
		return sysDepartService.selectDeptIds(Long.valueOf(deptId));
	}
}
