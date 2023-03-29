package com.llc.redis.method.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ErrorHandler;

/**
 * @author llc
 */

@Slf4j
public class StreamErrorHandler implements ErrorHandler {

    @Override
    public void handleError(Throwable t) {
        log.error("stream异常，异常原因：{}", t);
    }
}
