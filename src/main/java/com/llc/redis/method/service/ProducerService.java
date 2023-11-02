package com.llc.redis.method.service;


import com.llc.redis.method.constant.RedisKey;
import com.llc.redis.method.utils.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author llc
 */
@Slf4j
@Service
public class ProducerService {

    @Autowired
    IRedisService iRedisService;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    @Scheduled(cron = "30 * * * * ?")
    public void sendRecord() {
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(() -> record());
        }
    }

    private void record() {
        for (int i = 0; i < 100; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("serviceName", "licenseWorker");
            String result = iRedisService.addMap(RedisKey.STREAMKEY.code(), map);
            log.info("返回结果：{}", result);
        }
    }

}
