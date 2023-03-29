package com.llc.redis.method.init;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author llc
 */

@Slf4j
@Configuration
public class RegistThreadPool {

    /**
     * 产生线程池
     */
    @Bean("threadPoolExecutor")
    public ThreadPoolExecutor registThreadpool() {
        ThreadPoolExecutor executor = null;
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("llc-pool-%d").build();
        int corePoolSize = 16;
        int maximumPoolSize = 20;
        int keepAliveTime = 120;
        int workQueue = 10000;
        try {
            executor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(workQueue), namedThreadFactory);
        } catch (Exception e) {
            log.error("获取线程池配置参数出错,启用默认参数");
        } finally {
            if (executor == null) {
                executor = new ThreadPoolExecutor(corePoolSize,
                        maximumPoolSize,
                        keepAliveTime,
                        TimeUnit.SECONDS,
                        new ArrayBlockingQueue<Runnable>(workQueue), namedThreadFactory);
            }
        }
        return executor;
    }
}
