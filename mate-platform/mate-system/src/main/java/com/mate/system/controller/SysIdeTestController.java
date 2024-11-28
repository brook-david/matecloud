package com.mate.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.mate.core.common.api.Result;
import com.mate.core.ide.annotation.Ide;
import com.mate.core.ide.enums.IdeTypeEnum;

/**
 * @author xuzhanfu
 */
@RestController
public class SysIdeTestController {

	@GetMapping("/ide/test")
	@Ide(perFix = "TEST_", key = "key", ideTypeEnum = IdeTypeEnum.KEY)
	public Result<?> test(@RequestParam String key) {
		return Result.data(key);
	}
}
