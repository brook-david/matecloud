package com.mate.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mate.core.feign.annotation.EnableMateFeign;

/**
 * 单点登录启动类
 *
 * @author pangu
 */
@EnableMateFeign
@SpringBootApplication
public class SsoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SsoApplication.class, args);
	}
}
