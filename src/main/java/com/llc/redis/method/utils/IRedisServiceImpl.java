package com.llc.redis.method.utils;


import com.llc.redis.method.constant.RedisKey;
import io.lettuce.core.RedisException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.stream.StreamInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;

import static com.llc.redis.method.constant.RedisKey.STREAMKEY;


/**
 * @author llc
 */


@Slf4j
@Component
public class IRedisServiceImpl implements IRedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;


    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public Long del(String key, String... recordIds) {
        return redisTemplate.opsForStream().delete(key, recordIds);
    }

    @Override
    public void checkGroup(String groupName) {
        StreamInfo.XInfoGroups infoGroups = null;
        try {
            if (!hasKey(RedisKey.STREAMKEY.code())) {
                redisTemplate.opsForStream().createGroup(RedisKey.STREAMKEY.code(), groupName);
            }
            // 获取Stream的所有组信息
            infoGroups = redisTemplate.opsForStream().groups(STREAMKEY.code());
        } catch (RedisSystemException | RedisException | InvalidDataAccessApiUsageException ex) {
            log.error("group key not exist or commend error", ex);
        }

        boolean consumerExist = false;
        if (Objects.nonNull(infoGroups)) {
            if (infoGroups.stream().anyMatch(t -> Objects.equals(groupName, t.groupName()))) {
                consumerExist = true;
            }
        }
        // 创建不存在的分组
        if (!consumerExist) {
            redisTemplate.opsForStream().createGroup(RedisKey.STREAMKEY.code(), groupName);
        }
    }

    @Override
    public String addMap(String key, Map<String, Object> value) {
        return redisTemplate.opsForStream().add(key, value).getValue();
    }

    @Override
    public Long ack(String key, String group, String... recordIds) {
        return redisTemplate.opsForStream().acknowledge(key, group, recordIds);
    }

}
