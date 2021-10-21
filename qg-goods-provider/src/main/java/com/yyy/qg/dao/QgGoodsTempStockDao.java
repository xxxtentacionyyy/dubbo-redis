package com.yyy.qg.dao;

import com.yyy.qg.pojo.QgGoodsTempStock;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;

/**
 * (QgGoodsTempStock)表数据库访问层
 *
 * @author makejava
 * @since 2021-10-16 15:56:55
 */
public interface QgGoodsTempStockDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    QgGoodsTempStock queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param qgGoodsTempStock 查询条件
     * @param pageable         分页对象
     * @return 对象列表
     */
    List<QgGoodsTempStock> queryAllByLimit(QgGoodsTempStock qgGoodsTempStock, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param qgGoodsTempStock 查询条件
     * @return 总行数
     */
    long count(QgGoodsTempStock qgGoodsTempStock);

    /**
     * 新增数据
     *
     * @param qgGoodsTempStock 实例对象
     * @return 影响行数
     */
    int insert(QgGoodsTempStock qgGoodsTempStock);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<QgGoodsTempStock> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<QgGoodsTempStock> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<QgGoodsTempStock> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<QgGoodsTempStock> entities);

    /**
     * 修改数据
     *
     * @param qgGoodsTempStock 实例对象
     * @return 影响行数
     */
    int update(QgGoodsTempStock qgGoodsTempStock);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

    int getQgGoodsTempStockCountByMap(Map map);

}

