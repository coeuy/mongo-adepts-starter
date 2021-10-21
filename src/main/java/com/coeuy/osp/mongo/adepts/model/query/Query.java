package com.coeuy.osp.mongo.adepts.model.query;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public interface Query<R> extends Serializable {

    /**
     * 等于
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper eq(R column, Object value);

    /**
     * 不等于（单个比较）
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper ne(R column, Object value);

    /**
     * 多匹配（单个比较）
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper in(R column, Object value);

    /**
     * 大于等于（单个比较）
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper ge(R column, Object value);

    /**
     * 大于（单个比较）
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper gt(R column, Object value);

    /**
     * 小于等于（单个比较）
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper le(R column, Object value);

    /**
     * 小于（单个比较）
     *
     * @param column 注意不能使用多个比较值
     * @param value  注意不能使用多个比较值
     * @return Query
     */
    QueryWrapper lt(R column, Object value);


    /**
     * 范围查询（整型或者时间）
     *
     * @param column 字段
     * @param ge  大于等于
     * @param le  小于等于
     * @return Query
     */
    QueryWrapper scope(R column, Object ge, Object le);

    QueryWrapper phase(R column, Object ge, Object lt);

    /**
     * 模糊匹配(全部)
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    QueryWrapper like(R column, CharSequence keyword);


    /**
     * 模糊匹配（左边）
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    QueryWrapper likeLeft(R column, CharSequence keyword);

    /**
     * 模糊匹配（右边）
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    QueryWrapper likeRight(R column, CharSequence keyword);

    /**
     * 升序排序
     * @param column 字段
     * @return Query
     */
    QueryWrapper orderByAsc(R column);

    /**
     * 降序排序
     * @param column 字段
     * @return Query
     */
    QueryWrapper orderByDesc(R column);

    /**
     * 更新某个值
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper update(R column, Object value);

    /**
     * 更新某个值
     * @param column 字段
     * @param value 字段值
     * @return Query
     */
    QueryWrapper unUpdate(R column, Object value);

    /**
     * 索引搜索
     * @param value 搜索值
     * @return Query
     */
    QueryWrapper search(String value);

    /**
     * 追加子文档
     *
     * @return Query
     */
    QueryWrapper push(R column, Object value);

    /**
     * 删除子文档
     *
     * @return Query
     */
    QueryWrapper pull(R column, Object value);


    /**
     * 字段加减法
     *
     * @return Query
     */
    QueryWrapper inc(R column, Long number);
}
