package com.my.mall.controller;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.my.mall.annotation.AccessLimit;
import com.my.mall.common.BaseController;
import com.my.mall.common.Constants;
import com.my.mall.common.result.RespResult;
import com.my.mall.common.result.RespResultCode;
import com.my.mall.config.redis.RedisService;
import com.my.mall.entity.MyGoods;
import com.my.mall.entity.MySeckillOrder;
import com.my.mall.entity.MyUser;
import com.my.mall.entity.dto.BaseReqDTO;
import com.my.mall.service.MyGoodsService;
import com.my.mall.service.MyOrderService;
import com.my.mall.service.MySeckillOrderService;
import com.my.mall.service.MyUserService;
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

    private final MyGoodsService myGoodsService;

    private final MyUserService myUserService;

    private final MyOrderService myOrderService;

    private final MySeckillOrderService mySeckillOrderService;

    @Value("${spring.datasource.druid.maxActive}")
    private final Long maxActive = 0L;

    @AccessLimit(seconds = 1, maxCount = 1)
    @PostMapping("doBuy")
    public RespResult<RespResultCode> doBuy(BaseReqDTO baseReqDTO) {
        log.info("doBuy请求入参：{}", JSONUtil.toJsonStr(baseReqDTO));
        final Long userId = baseReqDTO.getUserId();
        final Long goodsId = baseReqDTO.getGoodsId();
        if (ObjectUtil.isAllEmpty(userId, goodsId)) {
            return RespResult.error(RespResultCode.ERR_PARAM_NOT_LEGAL);
        }

        //基本信息校验
        if (!checkBase(userId, goodsId)) {
            RespResult.error(RespResultCode.BUY_NOT_ESTABLISHED);
        }

        //3秒内用户请求总数不超过数据库最大连接数 4/5
        String userTotalRequestKey = Constants.REDIS_KEY + MyUser.USER_TOTAL_REQUEST;
        redisService.incr(userTotalRequestKey, 1L);
        int userTotalRequest = Convert.toInt(redisService.get(userTotalRequestKey), 0);
        if (userTotalRequest >= maxActive) {
            return RespResult.error(RespResultCode.REPEATED_BUSY);
        }
        redisService.expire(userTotalRequestKey, 3L);

        /**
         * 几种方案
         * 1.mysql 乐观锁，
         *
         * 2.mysql 悲观锁
         * 1.
         * 2.
         */
        return RespResult.success(RespResultCode.ORDER_QUEUING);
    }


    /**
     * 校验用户信息，商品信息，是否参与过秒杀
     *
     * @param userId
     * @param goodsId
     * @return
     */
    public boolean checkBase(Long userId, Long goodsId) {
        MyUser myUser = myUserService.queryById(userId);
        if (ObjectUtil.isNull(myUser) || 1 == myUser.getStatus()) {
            return false;
        }
        MyGoods myGoods = myGoodsService.queryById(goodsId);
        if (ObjectUtil.isNull(myGoods) || 1 == myGoods.getStatus() || myGoods.getStock() <= 0) {
            return false;
        }
        MySeckillOrder seckillOrder = mySeckillOrderService.queryById(userId, goodsId);
        return ObjectUtil.isNotNull(seckillOrder);
    }

}
