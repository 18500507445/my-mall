package com.my.mall.service.impl;

import com.my.mall.dao.MyGoodsDao;
import com.my.mall.entity.MyGoods;
import com.my.mall.service.MyGoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * @description: 商品表(MyGoods)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myGoodsService")
public class MyGoodsServiceImpl implements MyGoodsService {

    @Resource
    private MyGoodsDao myGoodsDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyGoods queryById(Long id) {
        return myGoodsDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param myGoods 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyGoods> queryList(MyGoods myGoods) {
        return myGoodsDao.queryList(myGoods);
    }

    /**
     * 新增数据
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyGoods myGoods) {
        return myGoodsDao.insert(myGoods);
    }

    /**
     * 修改数据
     *
     * @param myGoods 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyGoods myGoods) {
        return myGoodsDao.update(myGoods);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return myGoodsDao.deleteById(id) > 0;
    }
}
