package com.my.mall.service.impl;

import com.my.mall.dao.MyUserDao;
import com.my.mall.entity.MyUser;
import com.my.mall.service.MyUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wzh
 * @description: 用户表(MyUser)表服务实现类
 * @date 2023-04-15 15:53
 */
@Service("myUserService")
public class MyUserServiceImpl implements MyUserService {

    @Resource
    private MyUserDao myUserDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public MyUser queryById(Long id) {
        return myUserDao.queryById(id);
    }

    /**
     * 分页查询list
     *
     * @param myUser 筛选条件
     * @return 查询结果
     */
    @Override
    public List<MyUser> queryList(MyUser myUser) {
        return myUserDao.queryList(myUser);
    }

    /**
     * 新增数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    @Override
    public int insert(MyUser myUser) {
        return myUserDao.insert(myUser);
    }

    /**
     * 修改数据
     *
     * @param myUser 实例对象
     * @return 实例对象
     */
    @Override
    public int update(MyUser myUser) {
        return myUserDao.update(myUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return myUserDao.deleteById(id) > 0;
    }
}
