package com.llc.redis.method.module;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Redis任务
 */

@Data
public class RedisTask implements Serializable {
    private static final long serialVersionUID = 1L;

    private String taskID;
    private String serviceName;
    private Date updateTime;
    private Integer state;

}
