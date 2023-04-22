package com.mall.common.constant.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * trace http header 枚举
 * @author wzh
 */
@Getter
@AllArgsConstructor
public enum TraceHttpHeaderEnum {

    /**
     * header发送traceId
     */
    HEADER_TRACE_ID("header_trace_id", "http请求发送traceId");

    final String code;

    final String message;
}
