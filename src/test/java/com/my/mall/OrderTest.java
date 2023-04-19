package com.my.mall;

import com.alibaba.fastjson2.JSONObject;
import com.my.mall.dao.order.MyUserDao;
import com.my.mall.entity.order.MyUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @description:
 * @author: wzh
 * @date: 2023/4/15 23:00
 */
@SpringBootTest
public class OrderTest {

    @Resource
    private MyUserDao myUserDao;

    @Test
    public void testUser(){
        final MyUser myUser = myUserDao.queryById(1L);
        System.out.println("myUser = " + JSONObject.toJSONString(myUser));
    }


}
