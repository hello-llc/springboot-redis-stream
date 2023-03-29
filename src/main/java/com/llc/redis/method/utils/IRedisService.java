package com.llc.redis.method.utils;


import java.util.Map;

/**
 * @author llc
 */

public interface IRedisService {

    boolean hasKey(String key);

    Long del(String key, String... recordIds);

    void checkGroup(String groupName);

    String addMap(String key, Map<String, Object> value);

    Long ack(String key, String group, String... recordIds);

}
