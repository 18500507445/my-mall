package com.mall.pay.service;


import com.mall.pay.entity.MyPayList;

import java.util.List;

/**
 * @author wzh
 * @description: 支付列表(MyPayList)表服务接口
 * @date 2023-04-14 19:27
 */
public interface MyPayListService {

    /**
     * 查询支付列
     *
     * @param id 支付列主键
     * @return 支付列
     */
    MyPayList selectMyPayListById(Long id);

    /**
     * 查询支付列列表
     *
     * @param myPayList 支付列
     * @return 支付列集合
     */
    List<MyPayList> selectMyPayListList(MyPayList myPayList);

    /**
     * 新增支付列
     *
     * @param myPayList 支付列
     * @return 结果
     */
    int insertMyPayList(MyPayList myPayList);

    /**
     * 修改支付列
     *
     * @param myPayList 支付列
     * @return 结果
     */
    int updateMyPayList(MyPayList myPayList);

    /**
     * 批量删除支付列
     *
     * @param ids 需要删除的支付列主键集合
     * @return 结果
     */
    int deleteMyPayListByIds(String ids);

    /**
     * 删除支付列信息
     *
     * @param id 支付列主键
     * @return 结果
     */
    int deleteMyPayListById(Long id);

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int batchInsert(List<MyPayList> list);

}
