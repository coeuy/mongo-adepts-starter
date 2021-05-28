package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 查询对象包装器 </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 10:33
 */
@Data
public class QueryWrapper<T> implements Serializable {

    private static final long serialVersionUID = -8672871214331349354L;

    /**
     * PO类
     */
    private T entity;
    /**
     * 防止多次比较
     */
    private boolean compared;
    /**
     * 条件集合
     */
    private List<Wrapper> wrappers = new ArrayList<>();
    /**
     * 类型
     */
    private Class<T> entityClass;

    private String textSearch;

    /**
     * 等于
     *
     * @return Query
     */
    public QueryWrapper<T> eq(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.EQ, conditions));
        return this;
    }


    /**
     * 不等于（单个比较）
     *
     * @return Query
     */
    public QueryWrapper<T> ne(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.NE, conditions));
        return this;
    }

    /**
     * 多匹配（单个比较）
     *
     * @return Query
     */
    public QueryWrapper<T> in(String key, Object value) {
        if (!compared) {
            compared = true;
            Condition condition = new Condition(key, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.IN, conditions));
        }
        return this;
    }

    /**
     * 大于等于（单个比较）
     *
     * @return Query
     */
    public QueryWrapper<T> ge(String key, Object value) {
        if (!compared) {
            compared = true;
            Condition condition = new Condition(key, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.GE, conditions));
        }
        return this;
    }

    /**
     * 大于（单个比较）
     *
     * @return Query
     */
    public QueryWrapper<T> gt(String key, Object value) {
        if (!compared) {
            compared = true;
            Condition condition = new Condition(key, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.GT, conditions));
        }
        return this;
    }


    /**
     * 小于等于（单个比较）
     *
     * @return Query
     */
    public QueryWrapper<T> le(String key, Object value) {
        if (!compared) {
            compared = true;
            Condition condition = new Condition(key, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.LE, conditions));
        }
        return this;
    }

    /**
     * 小于（单个比较）
     *
     * @param key 注意不能使用多个比较值
     * @return Query
     */
    public QueryWrapper<T> lt(String key, Object value) {
        if (!compared) {
            compared = true;
            Condition condition = new Condition(key, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.LT, conditions));
        }
        return this;
    }

    /**
     * 模糊匹配(全部)
     *
     * @param key   注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param value 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    public QueryWrapper<T> like(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE, conditions));
        return this;
    }

    /**
     * 模糊匹配（左边）
     *
     * @param key   注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param value 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    public QueryWrapper<T> likeLeft(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE_LEFT, conditions));
        return this;
    }

    /**
     * 模糊匹配（右边）
     *
     * @param key   注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param value 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    public QueryWrapper<T> likeRight(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE_RIGHT, conditions));
        return this;
    }

    /**
     * 升序排序
     *
     * @return Query
     */
    public QueryWrapper<T> orderByAsc(String key) {
        Condition condition = new Condition(key, null);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.ORDER_BY_ASC, conditions));
        return this;
    }

    /**
     * 降序排序
     *
     * @return Query
     */
    public QueryWrapper<T> orderByDesc(String key) {
        Condition condition = new Condition(key, null);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.ORDER_BY_DESC, conditions));
        return this;
    }

    /**
     * 更新某个值
     *
     * @return Query
     */
    public QueryWrapper<T> update(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UPDATE, conditions));
        return this;
    }

    /**
     * 范围查询（整型或者时间）
     *
     * @param key 字段
     * @param ge  大于等于
     * @param le  小于等于
     * @return Query
     */
    public QueryWrapper<T> geAndLe(String key, Object ge, Object le) {
        this.wrappers.add(new Wrapper(Option.GE_AND_LE, key, ge, le));
        return this;
    }


    /**
     * 指定某个字段为空
     *
     * @return Query
     */
    public QueryWrapper<T> unUpdate(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UN_UPDATE, conditions));
        return this;
    }


    /**
     * 索引搜索
     *
     * @return Query
     */
    public QueryWrapper<T> search(String value) {
        this.setTextSearch(value);
        return this;
    }


    /**
     * 追加集合
     *
     * @return Query
     */
    public QueryWrapper<T> push(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.PUSH, conditions));
        return this;
    }


    /**
     * 指定某个字段为空
     *
     * @return Query
     */
    public QueryWrapper<T> or(QueryWrapper<T> wrapper) {
        this.wrappers.add(new Wrapper(Option.OR, wrapper));
        return this;
    }

    /**
     * 字段加减法
     *
     * @return Query
     */
    public QueryWrapper<T> inc(String key,Integer number) {
        Condition condition = new Condition(key, number);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UN_UPDATE, conditions));
        return this;
    }
}
