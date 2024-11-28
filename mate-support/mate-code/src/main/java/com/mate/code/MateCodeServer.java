package com.mate.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.mate.core.feign.annotation.EnableMateFeign;

/**
 * 代码生成启动类
 *
 * @author xuzhanfu
 * @date 2019-10-09 15:06
 **/
@EnableMateFeign
@SpringBootApplication
public class MateCodeServer {
    public static void main(String[] args) {
        SpringApplication.run(MateCodeServer.class, args);
    }
}