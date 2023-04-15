package com.my.mall.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import com.my.mall.annotation.AccessLimit;
import com.my.mall.common.BaseController;
import com.my.mall.common.Constants;
import com.my.mall.common.result.RespResult;
import com.my.mall.common.result.RespResultCode;
import com.my.mall.config.redis.RedisService;
import com.my.mall.entity.MyUser;
import com.my.mall.entity.dto.BaseReqDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 订单controller
 * @author: wzh
 * @date: 2023/4/15 15:55
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class OrderController extends BaseController {

    private final RedisService redisService;

    @Value("${spring.datasource.druid.maxActive}")
    private final Long maxActive = 0L;

    @AccessLimit(seconds = 1, maxCount = 1)
    @PostMapping("doBuy")
    public RespResult<JSONObject> doBuy(BaseReqDTO baseReqDTO) {
        log.info("doBuy请求入参：{}", JSONUtil.toJsonStr(baseReqDTO));
        final JSONObject jsonObject = new JSONObject();
        final Long userId = baseReqDTO.getUserId();
        final Long goodsId = baseReqDTO.getGoodsId();
        if (ObjectUtil.isAllEmpty(userId, goodsId)) {
            return RespResult.error(RespResultCode.ERR_PARAM_NOT_LEGAL);
        }
        //3秒内用户请求总数不超过数据库最大连接数 4/5
        redisService.incr(Constants.REDIS_KEY + MyUser.USER_TOTAL_REQUEST, 1L);
        int userTotalRequest = Convert.toInt(redisService.get(Constants.REDIS_KEY + MyUser.USER_TOTAL_REQUEST), 0);
        if (userTotalRequest >= maxActive) {
            return RespResult.error(RespResultCode.REPEATED_BUSY);
        }
        return RespResult.success(jsonObject);
    }

}
