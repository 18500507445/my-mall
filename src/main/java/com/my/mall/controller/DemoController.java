package com.my.mall.controller;

import com.my.mall.annotation.Lock;
import com.my.mall.common.result.RespResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: wzh
 * @date: 2023/04/15 17:19
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/")
public class DemoController {

    /**
     * redisson
     */
    @Lock(value = "redisson", expireSeconds = 60)
    @PostMapping("redisson")
    public RespResult<Void> redisson() {
        return RespResult.success();
    }

}
