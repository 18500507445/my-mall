package com.mall.pay.service.pay.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mall.common.config.RedisService;
import com.mall.common.constant.Constants;
import com.mall.common.constant.result.AjaxResult;
import com.mall.common.enums.PayWayEnum;
import com.mall.pay.entity.MyPayConfig;
import com.mall.pay.entity.MyPayOrder;
import com.mall.pay.entity.param.KsPayParam;
import com.mall.pay.entity.param.PayParam;
import com.mall.pay.payment.open.KsPayment;
import com.mall.pay.service.MyPayConfigService;
import com.mall.pay.service.MyPayOrderService;
import com.mall.pay.service.pay.PayService;
import com.yeepay.shade.org.apache.commons.collections4.MapUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description: 快手支付实现类
 * @author: wzh
 * @date: 2023/3/15 14:04
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class KsPayServiceImpl implements PayService {

    private final KsPayment ksPayment;

    private final RedisService redisService;

    private final MyPayConfigService myPayConfigService;

    private final MyPayOrderService myPayOrderService;

    @Override
    public PayWayEnum getPayType() {
        return PayWayEnum.KS_PAY;
    }

    @Override
    public AjaxResult pay(PayParam param) {
        String userName = param.getUserName();
        String totalAmount = param.getTotalAmount();
        String source = param.getSource();
        String sid = param.getSid();
        String orderId = param.getOrderId();
        String productId = param.getProductId();
        Long payConfigId = param.getPayConfigId();
        int businessCode = param.getBusinessCode();
        //9微信(四方支付) 10支付宝(四方支付)
        int type = Integer.parseInt(param.getType());
        String subject = param.getSubject();
        String openId = param.getOpenId();
        if (ObjectUtil.hasEmpty(source, sid, payConfigId, orderId, type, userName, openId, totalAmount, productId, subject, businessCode)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        try {
            //查询订单
            MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, 6);
            if (ObjectUtil.isNotNull(payOrder)) {
                return AjaxResult.error("1000", "订单已存在,请从新下单");
            } else {
                //1.0 获取支付配置
                MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
                if (ObjectUtil.isNull(payConfig)) {
                    return AjaxResult.error("1000", "没有查询到支付方式");
                }
                MyPayOrder insert = MyPayOrder.builder().source(source)
                        .sid(sid)
                        .orderId(orderId)
                        .productId(productId)
                        .payConfigId(payConfigId)
                        .userName(userName)
                        .tradeStatus(0)
                        .payWay(6)
                        .type(type)
                        .businessCode(businessCode)
                        .operator(openId)
                        .fee(new BigDecimal(totalAmount))
                        .build();

                AjaxResult accessTokenResult = getOpenId(param);
                if (ObjectUtil.isNotNull(accessTokenResult)) {
                    JSONObject jsonObject = JSONUtil.parseObj(accessTokenResult);
                    if (jsonObject.containsKey("data") && StrUtil.isNotBlank(jsonObject.getStr("data"))) {
                        //2.0 创建订单
                        KsPayParam data = KsPayParam.builder()
                                .appId(payConfig.getAppId())
                                .appSecret(payConfig.getAppSecret())
                                .accessToken(jsonObject.getStr("data"))
                                .outOrderNo(orderId)
                                .openId(openId)
                                .totalAmount(NumberUtil.mul(totalAmount, "100").longValue())
                                .subject(subject)
                                .payType(type)
                                .notifyUrl(payConfig.getNotifyUrl())
                                .build();
                        Map<String, Object> orderMap = ksPayment.tradeOrder(data);
                        if (!orderMap.isEmpty() && ObjectUtil.equals(Constants.KS_CODE, MapUtils.getIntValue(orderMap, "result", 0))) {
                            int flag = myPayOrderService.insertMyPayOrder(insert);
                            if (0 == flag) {
                                return AjaxResult.error("1000", "创建支付订单失败,请从新下单");
                            }
                            return AjaxResult.success(orderMap.get("order_info"));
                        } else {
                            return AjaxResult.error(MapUtil.getStr(orderMap, "error_msg"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("ksPay异常：{}", e.getMessage());
        }
        return AjaxResult.error();
    }

    /**
     * 退款
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult refund(PayParam param) {
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(payConfigId)) {
            return AjaxResult.error("1000", "缺少必要参数payConfigId");
        }
        //获取支付配置
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AjaxResult accessTokenResult = getOpenId(param);
        if (ObjectUtil.isNotNull(accessTokenResult)) {
            JSONObject jsonObject = JSONUtil.parseObj(accessTokenResult);
            if (jsonObject.containsKey("data") && StrUtil.isNotBlank(jsonObject.getStr("data"))) {
                KsPayParam data = KsPayParam.builder()
                        .appId(payConfig.getAppId())
                        .appSecret(payConfig.getAppSecret())
                        .accessToken(jsonObject.getStr("data"))
                        .outOrderNo(param.getOrderId())
                        .build();
                return AjaxResult.success(ksPayment.refund(data));
            }
        }
        return AjaxResult.success();
    }

    /**
     * 查询订单
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult queryOrder(PayParam param) {
        String orderId = param.getOrderId();
        Integer payWay = param.getPayWay();
        if (ObjectUtil.hasEmpty(orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        MyPayOrder payOrder = myPayOrderService.selectMyPayOrderByOrderIdAndPayWay(orderId, payWay);
        if (ObjectUtil.isNull(payOrder)) {
            return AjaxResult.error("1000", "没有查询到订单信息");
        }
        //获取支付配置
        MyPayConfig payConfig = myPayConfigService.getConfigData(payOrder.getPayConfigId());
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        param.setPayConfigId(payOrder.getPayConfigId());
        AjaxResult accessTokenResult = getOpenId(param);
        if (ObjectUtil.isNotNull(accessTokenResult)) {
            JSONObject jsonObject = JSONUtil.parseObj(accessTokenResult);
            if (jsonObject.containsKey("data") && StrUtil.isNotBlank(jsonObject.getStr("data"))) {
                KsPayParam data = KsPayParam.builder()
                        .appId(payConfig.getAppId())
                        .appSecret(payConfig.getAppSecret())
                        .accessToken(jsonObject.getStr("data"))
                        .outOrderNo(orderId)
                        .build();
                return AjaxResult.success(ksPayment.queryOrder(data));
            }
        }
        return AjaxResult.error();
    }

    /**
     * 获取accessToken(接口凭证)
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult getOpenId(PayParam param) {
        Object accessToken = "";
        Long payConfigId = param.getPayConfigId();
        if (ObjectUtil.hasEmpty(payConfigId)) {
            return AjaxResult.error("1000", "缺少必要参数payConfigId");
        }
        //获取支付配置
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        String key = Constants.REDIS_KEY + "getAccessToken_" + payConfig.getId();
        Object o = redisService.get(key);
        if (ObjectUtil.isNotNull(o)) {
            accessToken = o.toString();
        } else {
            KsPayParam data = KsPayParam.builder()
                    .appId(payConfig.getAppId())
                    .appSecret(payConfig.getAppSecret())
                    .build();
            Map<String, Object> tokenMap = ksPayment.getAccessToken(data);
            if (MapUtil.isNotEmpty(tokenMap) && ObjectUtil.equals(1, MapUtil.getInt(tokenMap, "result", 0))) {
                accessToken = MapUtil.getStr(tokenMap, "access_token");
                redisService.set(key, accessToken, MapUtil.getLong(tokenMap, "expires_in") - 3600);
            }
        }
        return AjaxResult.success(accessToken);
    }

    /**
     * 关闭订单
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult closeOrder(PayParam param) {
        Long payConfigId = param.getPayConfigId();
        String orderId = param.getOrderId();
        if (ObjectUtil.hasEmpty(payConfigId, orderId)) {
            return AjaxResult.error("1000", "缺少必要参数");
        }
        //获取支付配置
        MyPayConfig payConfig = myPayConfigService.getConfigData(payConfigId);
        if (ObjectUtil.isNull(payConfig)) {
            return AjaxResult.error("1000", "没有查询到支付方式");
        }
        AjaxResult accessTokenResult = getOpenId(param);
        if (ObjectUtil.isNotNull(accessTokenResult)) {
            JSONObject jsonObject = JSONUtil.parseObj(accessTokenResult);
            if (jsonObject.containsKey("data") && StrUtil.isNotBlank(jsonObject.getStr("data"))) {
                KsPayParam data = KsPayParam.builder()
                        .appId(payConfig.getAppId())
                        .appSecret(payConfig.getAppSecret())
                        .accessToken(jsonObject.getStr("data"))
                        .outOrderNo(param.getOrderId())
                        .build();
                return AjaxResult.success(ksPayment.cancelOrder(data));
            }
        }
        return AjaxResult.success();
    }

    /**
     * 商家转账到个人
     *
     * @param param
     * @return
     */
    @Override
    public AjaxResult merchantTransfer(PayParam param) {
        return AjaxResult.success("接口待开发。。。");
    }
}
