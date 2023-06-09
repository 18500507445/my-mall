package com.mall.order.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.mall.common.constant.context.BodyReaderRequestWrapper;
import com.mall.common.constant.context.ReqGetBody;
import com.mall.common.constant.context.RequestParamsUtil;
import com.mall.common.constant.trace.Trace;
import com.mall.common.constant.trace.TraceHelper;
import com.mall.common.constant.trace.TraceHttpHeaderEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @description: web端链路过滤器处理(设置traceId, spanId)
 * @author: wzh
 * @date: 2023/03/20 11:16
 */
@Component
@Slf4j
public class TraceWebFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest request = (HttpServletRequest) req;
        String traceId = String.valueOf(request.getHeader(TraceHttpHeaderEnum.HEADER_TRACE_ID.getCode()));
        if (StrUtil.isNotEmpty(traceId) && !"null".equals(traceId)) {
            TraceHelper.setCurrentTrace(traceId);
        } else {
            TraceHelper.getCurrentTrace();
        }
        BodyReaderRequestWrapper requestWrapper = printAccessLog(request);
        String dTraceId = TraceHelper.getCurrentTrace().getTraceId();
        log.debug("trace web filter-traceId:{}", dTraceId);
        filterChain.doFilter(requestWrapper != null ? requestWrapper : request, resp);
        MDC.put(Trace.PARENT_SPAN, TraceHelper.genSpanId());
        log.info("当前请求总耗时：{}ms", System.currentTimeMillis() - start);
        TraceHelper.clearCurrentTrace();
    }

    /**
     * 打印访问日志
     */
    @SuppressWarnings({"unchecked"})
    private BodyReaderRequestWrapper printAccessLog(HttpServletRequest request) throws IOException {
        BodyReaderRequestWrapper requestWrapper;
        String requestUrl = request.getRequestURI();
        SortedMap<String, Object> paramResult = new TreeMap<>(RequestParamsUtil.getUrlParams(request));
        try {
            if (!StrUtil.equals(HttpMethod.GET.name(), request.getMethod())) {
                String contentType = request.getContentType();
                if (ObjectUtil.isNotNull(contentType)) {
                    if (ObjectUtil.isNotNull(contentType)) {
                        if (StrUtil.containsIgnoreCase(contentType, "json")) {
                            requestWrapper = new BodyReaderRequestWrapper(request);
                            paramResult.putAll(JSONObject.parseObject(ReqGetBody.getBody(requestWrapper), Map.class));
                            return requestWrapper;
                        } else if (StrUtil.containsIgnoreCase(contentType, "form")) {
                            paramResult.putAll(RequestParamsUtil.getFormParams(request));
                        }
                    }
                }
            }
        } finally {
            log.info("请求apiName：{}，方式：{}，body：{}", requestUrl, request.getMethod(), JSONObject.toJSONString(paramResult));
        }
        return null;
    }
}