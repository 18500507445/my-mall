package com.mall.pay.dao;

import com.mall.pay.entity.MyPayConfig;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author wzh
 * @description: 支付配置表(MyPayConfig)表数据库访问层
 * @date 2023-04-14 19:27
 */
@Mapper
public interface MyPayConfigDao {

    /**
     * 查询支付配置
     *
     * @param id 支付配置主键
     * @return 支付配置
     */
    MyPayConfig selectMyPayConfigById(Long id);

    /**
     * 查询支付配置列表
     *
     * @param myPayConfig 支付配置
     * @return 支付配置集合
     */
    List<MyPayConfig> selectMyPayConfigList(MyPayConfig myPayConfig);

    /**
     * 新增支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    int insertMyPayConfig(MyPayConfig myPayConfig);

    /**
     * 修改支付配置
     *
     * @param myPayConfig 支付配置
     * @return 结果
     */
    int updateMyPayConfig(MyPayConfig myPayConfig);

    /**
     * 删除支付配置
     *
     * @param id 支付配置主键
     * @return 结果
     */
    int deleteMyPayConfigById(Long id);

    /**
     * 批量删除支付配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    int deleteMyPayConfigByIds(String[] ids);

}

