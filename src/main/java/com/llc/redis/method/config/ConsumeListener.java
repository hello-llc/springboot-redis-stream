package com.llc.redis.method.config;

import com.llc.redis.method.utils.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author llc
 */

@Slf4j
@Component
public class ConsumeListener implements StreamListener<String, MapRecord<String, String, String>> {

    @Autowired
    IRedisService iRedisService;

    private static ConsumeListener consumeListener;

    @PostConstruct
    public void init() {
        consumeListener = this;
        consumeListener.iRedisService = this.iRedisService;
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String stream = message.getStream();
        RecordId id = message.getId();
        Map<String, String> map = message.getValue();
        log.info("[不自动ack] 接收到一个消息 stream:[{}],id:[{}],value:[{}]", stream, id, map);
        consumeListener.iRedisService.ack(stream, "group", id.getValue());
        consumeListener.iRedisService.del(stream, id.getValue());
    }
}
