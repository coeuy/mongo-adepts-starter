package com.coeuy.osp.mongo.adepts.model.query;

import com.coeuy.osp.mongo.adepts.constants.Option;
import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.utils.FieldUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class LambdaQueryAdepts<T>  extends QueryWrapper implements Query<FieldGetter<T, ?>>, Serializable {


    /**
     * 等于
     *
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> eq(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> ne(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> in(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            String fieldName = FieldUtils.getFieldName(column);
            compared = true;
            Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> ge(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            String fieldName = FieldUtils.getFieldName(column);
            compared = true;
            Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> gt(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            String fieldName = FieldUtils.getFieldName(column);
            compared = true;
            Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> le(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            String fieldName = FieldUtils.getFieldName(column);
            compared = true;
            Condition condition = new Condition(fieldName, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.LE, conditions));
        }
        return this;
    }

    /**
     * 小于（单个比较）
     *
     * @param column 注意不能使用多个比较值
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> lt(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            String fieldName = FieldUtils.getFieldName(column);
            compared = true;
            Condition condition = new Condition(fieldName, value);
            List<Condition> conditions = new ArrayList<>();
            conditions.add(condition);
            this.wrappers.add(new Wrapper(Option.LT, conditions));
        }
        return this;
    }

    /**
     * 范围查询（整型或者时间）
     *
     * @param column 字段
     * @param ge  大于等于
     * @param le  小于等于
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> geAndLe(FieldGetter<T, ?> column, Object ge, Object le) {
        String fieldName = FieldUtils.getFieldName(column);
        this.wrappers.add(new Wrapper(Option.GE_AND_LE, fieldName, ge, le));
        return this;
    }

    /**
     * 模糊匹配(全部)
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> like(FieldGetter<T, ?> column, CharSequence keyword) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, keyword);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE, conditions));
        return this;
    }

    /**
     * 模糊匹配（左边）
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> likeLeft(FieldGetter<T, ?> column, CharSequence keyword) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, keyword);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.LIKE_LEFT, conditions));
        return this;
    }

    /**
     * 模糊匹配（右边）
     *
     * @param column     注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @param keyword    注意！模糊匹配只能匹配字符串类型，整型无法模糊查询
     * @return Query
     */

    @Override
    public LambdaQueryAdepts<T> likeRight(FieldGetter<T, ?> column, CharSequence keyword) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, keyword);
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
    public LambdaQueryAdepts<T> orderByAsc(FieldGetter<T, ?> column) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, null);
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
    public LambdaQueryAdepts<T> orderByDesc(FieldGetter<T, ?> column) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, null);
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
    public LambdaQueryAdepts<T> update(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> unUpdate(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> search(String value) {
        this.setTextSearch(value);
        return this;
    }


    /**
     * 追加子文档
     *
     * @return Query
     */
    @Override
    public LambdaQueryAdepts<T> push(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> pull(FieldGetter<T, ?> column, Object value) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, value);
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
    public LambdaQueryAdepts<T> inc(FieldGetter<T, ?> column, Integer number) {
        String fieldName = FieldUtils.getFieldName(column);
        Condition condition = new Condition(fieldName, number);
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition);
        this.wrappers.add(new Wrapper(Option.UN_UPDATE, conditions));
        return this;
    }
}
