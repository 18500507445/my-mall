package com.mall.order.config.redisson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: Redisson配置映射类
 * @author: wzh
 * @date: 2023/03/19 17:18
 */
@Data
@ConfigurationProperties(prefix = "redisson.lock.server")
public class RedissonProperties {

    /**
     * redis主机地址，ip：port，有多个用半角逗号分隔
     */
    private String address;
    /**
     * 连接类型，支持standalone-单机节点，sentinel-哨兵，cluster-集群，masterslave-主从
     */
    private String type;
    /**
     * redis连接密码
     */
    private String password;
    /**
     * 选取数据库
     */
    private int database;

}
