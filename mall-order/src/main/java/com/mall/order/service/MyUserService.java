package com.mall.order.service;


import com.mall.order.entity.MyUser;

import java.util.List;

/**
 * @author wzh
 * @description: 用户表(MyUser)表服务接口
 * @date 2023-04-15 15:53
 */
public interface MyUserService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    MyUser queryById(Long id);

    /**
     * 分页查询list
     *
     * @param myUser 筛选条件
     * @return 查询结果
     */
    List<MyUser> queryList(MyUser myUser);

    /**
     * 新增数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    int insert(MyUser myUser);

    /**
     * 修改数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    int update(MyUser myUser);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
