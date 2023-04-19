package com.my.mall.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: 返回ok 目的检测服务存活状态
 * @author: wzh
 * @date: 2023/4/14 18:56
 */
public class MonitorController {

    @RequestMapping(value = "monitor.jsp")
    public String test() {
        return "ok";
    }
}
