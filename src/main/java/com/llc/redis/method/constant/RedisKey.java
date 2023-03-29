package com.llc.redis.method.constant;

/**
 * @author llc
 */

public enum RedisKey {
    // streamKey
    STREAMKEY("llc:redis:stream:string");


    private String code;

    RedisKey(String code) {
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    @Override
    public String toString() {
        return this.code;
    }
}
