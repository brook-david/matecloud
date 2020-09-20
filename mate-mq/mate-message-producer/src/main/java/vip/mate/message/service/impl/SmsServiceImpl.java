package vip.mate.message.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import vip.mate.core.rocketmq.channel.MateSource;
import vip.mate.message.service.ISmsService;

/**
 * 发送短信实现类
 *
 * @author xuzhanfu
 */
@Service
@AllArgsConstructor
public class SmsServiceImpl implements ISmsService {

	private final MateSource source;

	@Override
	public void sendSms(String message) {

		source.smsOutput().send(MessageBuilder.withPayload(message).build());

	}
}
