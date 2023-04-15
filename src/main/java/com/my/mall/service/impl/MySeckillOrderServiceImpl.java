package com.my.mall.service.impl;

import com.my.mall.dao.MySeckillOrderDao;
import com.my.mall.entity.MySeckillOrder;
import com.my.mall.service.MySeckillOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * @description: 秒杀订单表(MySeckillOrder)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("mySeckillOrderService")
public class MySeckillOrderServiceImpl implements MySeckillOrderService {

    @Resource
    private MySeckillOrderDao mySeckillOrderDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MySeckillOrder queryById(Long id) {
        return mySeckillOrderDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param mySeckillOrder 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MySeckillOrder> queryList(MySeckillOrder mySeckillOrder) {
        return mySeckillOrderDao.queryList(mySeckillOrder);
    }

    /**
     * 新增数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MySeckillOrder mySeckillOrder) {
        return mySeckillOrderDao.insert(mySeckillOrder);
    }

    /**
     * 修改数据
     *
     * @param mySeckillOrder 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MySeckillOrder mySeckillOrder) {
        return mySeckillOrderDao.update(mySeckillOrder);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return mySeckillOrderDao.deleteById(id) > 0;
    }
}
