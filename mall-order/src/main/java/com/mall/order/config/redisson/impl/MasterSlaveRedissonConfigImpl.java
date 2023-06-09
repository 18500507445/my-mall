package com.mall.order.config.redisson.impl;

import com.mall.common.enums.RedissonEnum;
import com.mall.order.config.redisson.RedissonConfigStrategy;
import com.mall.order.config.redisson.RedissonProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.redisson.config.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * 主从方式Redisson配置
 * 连接方式：主节点,子节点,子节点, 主节点一定要在第一位
 * 格式为: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
 *
 * @author wzh
 * @date 2023/03/26 23:10
 */
@Slf4j
public class MasterSlaveRedissonConfigImpl implements RedissonConfigStrategy {

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String masterNodeAddr = addrTokens[0];
            // 设置主节点ip
            config.useMasterSlaveServers().setMasterAddress(masterNodeAddr);
            if (StringUtils.isNotBlank(password)) {
                config.useMasterSlaveServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);
            // 设置从节点，移除第一个节点，默认第一个为主节点
            List<String> slaveList = new ArrayList<>();
            for (String addrToken : addrTokens) {
                slaveList.add(RedissonEnum.REDIS_CONNECTION_PREFIX.getConstant_value() + addrToken);
            }
            slaveList.remove(0);

            config.useMasterSlaveServers().addSlaveAddress((String[]) slaveList.toArray());
            log.info("初始化[MASTER_SLAVE]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            log.error("MASTER_SLAVE Redisson init error：{}", e.getMessage());
        }
        return config;
    }
}
