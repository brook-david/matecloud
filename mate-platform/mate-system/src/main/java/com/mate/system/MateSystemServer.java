package com.mate.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mate.core.feign.annotation.EnableMateFeign;

/**
 * 管理系统后台启动类
 * @author xzf
 */
@EnableMateFeign
@SpringBootApplication
public class MateSystemServer {
    public static void main(String[] args) {
        SpringApplication.run(MateSystemServer.class, args);
    }
}
