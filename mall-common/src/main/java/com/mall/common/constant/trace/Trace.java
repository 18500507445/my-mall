package com.mall.common.constant.trace;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: Trace对象
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@Data
public class Trace implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String TRACE = "X-B3-TraceId";

    public static final String PARENT_SPAN = "X-B3-SpanId";

    /**
     * 分布式traceId
     */
    private String traceId;

    /**
     * 分布式spanId
     */
    private String spanId;
}

