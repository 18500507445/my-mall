package com.my.mall.common;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONObject;
import com.my.mall.annotation.AccessLimit;
import com.my.mall.common.result.RespResult;
import com.my.mall.config.redis.RedisService;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @description: 限流
 * @author: wzh
 * @date: 2023/3/02 16:34
 */
@Component
public class LimitInterceptor extends BaseController implements HandlerInterceptor {

    @Resource
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            //获取方法中的注解,看是否有该注解
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            String message = accessLimit.message();
            String ip = getIp();
            if (StrUtil.isNotBlank(ip)) {
                String key = Constants.REDIS_KEY + "limit_userIp_" + ip;
                Long value = redisService.incr(key, 1L);
                redisService.expire(key, Convert.toLong(seconds));
                if (value > maxCount) {
                    //超出访问次数，返回
                    render(response, message);
                    return false;
                }
            }
        }
        return true;

    }

    private void render(HttpServletResponse response, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSONObject.toJSONString(RespResult.error(message));
        out.write(str.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
