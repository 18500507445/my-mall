package com.mall.common.enums;

/**
 * @description: 业务枚举类
 * @author: wzh
 * @date: 2023/4/14 19:18
 */
public enum BusinessEnum {

    DEFAULT(0, "默认"),

    RECHARGE(1, "充值");

    /**
     * 业务code
     */
    private final int code;

    /**
     * 业务名称
     */
    private final String name;

    BusinessEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public static BusinessEnum valuesOf(int code) {
        switch (code) {
            case 1:
                return RECHARGE;
            default:
                return DEFAULT;
        }
    }
}
