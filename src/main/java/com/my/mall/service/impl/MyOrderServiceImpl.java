package com.my.mall.service.impl;

import com.my.mall.dao.MyOrderDao;
import com.my.mall.entity.MyOrder;
import com.my.mall.service.MyOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * @description: 订单表(MyOrder)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myOrderService")
public class MyOrderServiceImpl implements MyOrderService {

    @Resource
    private MyOrderDao myOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyOrder queryById(Long id) {
        return myOrderDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param myOrder 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyOrder> queryList(MyOrder myOrder) {
        return myOrderDao.queryList(myOrder);
    }

    /**
     * 新增数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyOrder myOrder) {
        return myOrderDao.insert(myOrder);
    }

    /**
     * 修改数据
     *
     * @param myOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyOrder myOrder) {
        return myOrderDao.update(myOrder);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return myOrderDao.deleteById(id) > 0;
    }
}
