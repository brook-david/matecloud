package com.mate.core.log.event;

import org.springframework.context.ApplicationEvent;
import com.mate.core.common.dto.CommonLog;

/**
 * 日志事件
 * @author pangu 7333791@qq.com
 * @since 2020-7-15
 */
public class LogEvent extends ApplicationEvent {

    public LogEvent(CommonLog source) {
        super(source);
    }
}
