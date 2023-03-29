package com.llc.redis.method.config;

import com.llc.redis.method.constant.RedisKey;
import com.llc.redis.method.utils.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author llc
 */

@Slf4j
@Configuration
public class RedisStreamConfig {

    @Autowired
    IRedisService iRedisService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    @Bean
    public Subscription subscription(RedisConnectionFactory factory) {
        iRedisService.checkGroup("group");
        // 创建Stream消息监听容器配置
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, MapRecord<String, String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 一次最多获取多少条消息
                        .batchSize(1)
                        // 运行 Stream 的 poll task
                        .executor(threadPoolExecutor)
                        // Stream 中没有消息时，阻塞多长时间，需要比 `spring.redis.timeout` 的时间小
                        .pollTimeout(Duration.ofSeconds(15))
                        // 获取消息的过程或获取到消息给具体的消息者处理的过程中，发生了异常的处理
                        .errorHandler(new StreamErrorHandler())
                        .build();
        // 创建Stream消息监听容器
        StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamMessageListenerContainer =
                StreamMessageListenerContainer.create(factory, options);

        // 消费组,自动ack
        Subscription subscription = streamMessageListenerContainer.receiveAutoAck(Consumer.from("group", "comsumer"),
                StreamOffset.create(RedisKey.STREAMKEY.code(), ReadOffset.lastConsumed()), new ConsumeAckListener());

        // 消费组,不自动ack
//        Subscription subscription = streamMessageListenerContainer.receive(Consumer.from("group", "consumer"),
//                StreamOffset.create(RedisKey.STREAMKEY.code(), ReadOffset.lastConsumed()), new ConsumeListener());

        // 监听容器启动
        streamMessageListenerContainer.start();
        return subscription;
    }

}
