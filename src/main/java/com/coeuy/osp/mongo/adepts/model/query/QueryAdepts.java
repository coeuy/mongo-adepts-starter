package com.coeuy.osp.mongo.adepts.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询对象包装器
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 10:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QueryAdepts extends QueryWrapper implements Query<String> ,Serializable {

    private static final long serialVersionUID = -8672871214331349354L;


    /**
     * 等于
     *
     * @return Query
     */
    @Override
    public QueryAdepts eq(String key, Object value) {
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
    @Override
    public QueryAdepts ne(String key, Object value) {
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
    @Override
    public QueryAdepts in(String key, Object value) {
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
    @Override
    public QueryAdepts ge(String key, Object value) {
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
    @Override
    public QueryAdepts gt(String key, Object value) {
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
    @Override
    public QueryAdepts le(String key, Object value) {
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
    @Override
    public QueryAdepts lt(String key, Object value) {
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
     * 范围查询（整型或者时间）
     *
     * @param key 字段
     * @param ge  大于等于
     * @param le  小于等于
     * @return Query
     */
    @Override
    public QueryAdepts geAndLe(String key, Object ge, Object le) {
        this.wrappers.add(new Wrapper(Option.GE_AND_LE, key, ge, le));
        return this;
    }

    /**
     * 模糊匹配(全部)
     *
     * @param key     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    @Override
    public QueryAdepts like(String key, CharSequence keyword) {
        Condition condition = new Condition(key, keyword);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE, conditions));
        return this;
    }

    /**
     * 模糊匹配（左边）
     *
     * @param key     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    @Override
    public QueryAdepts likeLeft(String key, CharSequence keyword) {
        Condition condition = new Condition(key, keyword);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE_LEFT, conditions));
        return this;
    }

    /**
     * 模糊匹配（右边）
     *
     * @param key     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword 注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */

    @Override
    public QueryAdepts likeRight(String key, CharSequence keyword) {
        Condition condition = new Condition(key, keyword);
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
    @Override
    public QueryAdepts orderByAsc(String key) {
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
    @Override
    public QueryAdepts orderByDesc(String key) {
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
    @Override
    public QueryAdepts update(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UPDATE, conditions));
        return this;
    }


    /**
     * 指定某个字段为空
     *
     * @return Query
     */
    @Override
    public QueryAdepts unUpdate(String key, Object value) {
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
    @Override
    public QueryAdepts search(String value) {
        this.setTextSearch(value);
        return this;
    }


    /**
     * 追加子文档
     *
     * @return Query
     */
    @Override
    public QueryAdepts push(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.PUSH, conditions));
        return this;
    }

    /**
     * 删除子文档
     *
     * @return Query
     */
    @Override
    public QueryAdepts pull(String key, Object value) {
        Condition condition = new Condition(key, value);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.PUSH, conditions));
        return this;
    }

    @Override
    public QueryWrapper or(QueryWrapper wrapper) {
        this.wrappers.add(new Wrapper(Option.OR, wrapper));
        return this;
    }



    /**
     * 字段加减法
     *
     * @return Query
     */
    @Override
    public QueryAdepts inc(String key, Integer number) {
        Condition condition = new Condition(key, number);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UN_UPDATE, conditions));
        return this;
    }
}
