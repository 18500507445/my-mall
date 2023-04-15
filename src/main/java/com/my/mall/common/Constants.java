package com.my.mall.common;

/**
 * @description: 常量类
 * @author: wzh
 * @date: 2023/4/14 18:57
 */
public class Constants {

    public static final String ZERO = "0";

    public static final String SUCCESS = "SUCCESS";

    public static final String FAIL = "FAIL";

    public static final String TRADE_SUCCESS = "TRADE_SUCCESS";

    public static final String OK = "ok";

    public static final String REDIS_KEY = "my-mall:";

    /**
     * 直连交换机(自带)
     */
    public static final String EXCHANGE_DIRECT = "amq.direct";

    /**
     * 死信交换机
     */
    public static final String EXCHANGE_DIRECT_DLX = "dlx.direct";

    /**
     * 死信队列
     */
    public static final String QUEUE_DIRECT_DLX = "queue-direct-dlx";

    /**
     * 异步下单队列
     */
    public static final String QUEUE_DIRECT_CREATE_ORDER = "queue-direct-createOrder";

    /**
     * 查询订单队列
     */
    public static final String QUEUE_DIRECT_QUERY_ORDER = "queue-direct-queryOrder";


}
