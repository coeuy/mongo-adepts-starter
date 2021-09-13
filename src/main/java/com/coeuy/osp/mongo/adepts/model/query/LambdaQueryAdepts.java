package com.coeuy.osp.mongo.adepts.model.query;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.constants.Option;
import com.coeuy.osp.mongo.adepts.utils.LambdaUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class LambdaQueryAdepts<T>  extends QueryWrapper implements Query<FieldGetter<T, ?>>, Serializable {

    public LambdaQueryAdepts() {
        this((T) null);
    }

    public LambdaQueryAdepts(T entity) {
        this.entity = entity;
    }

    @Getter
    @Setter
    private T entity;

    public LambdaQueryAdepts(Class<T> entityClass) {
        setEntityClass(entityClass);
    }

    @Override
    public LambdaQueryAdepts<T> eq(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildEq(LambdaUtils.getFieldName(column), value));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> ne(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildNe(LambdaUtils.getFieldName(column), value));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> in(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildIn(LambdaUtils.getFieldName(column), value));
        }
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> ge(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildGe(LambdaUtils.getFieldName(column), value));
        }
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> gt(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildGt(LambdaUtils.getFieldName(column), value));
        }
        return this;
    }



    @Override
    public LambdaQueryAdepts<T> le(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildLe(LambdaUtils.getFieldName(column), value));
        }
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> lt(FieldGetter<T, ?> column, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildLt(LambdaUtils.getFieldName(column), value));
        }
        return this;
    }

    @Override
    public LambdaQueryAdepts<T> scope(FieldGetter<T, ?> column, Object ge, Object le) {
        this.wrappers.add(buildScope(LambdaUtils.getFieldName(column), ge, le));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> like(FieldGetter<T, ?> column, CharSequence keyword) {
        this.wrappers.add(buildLike(LambdaUtils.getFieldName(column), keyword));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> likeLeft(FieldGetter<T, ?> column, CharSequence keyword) {
        this.wrappers.add(buildLikeLeft(LambdaUtils.getFieldName(column), keyword));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> likeRight(FieldGetter<T, ?> column, CharSequence keyword) {
        this.wrappers.add(buildLikeRight(LambdaUtils.getFieldName(column), keyword));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> orderByAsc(FieldGetter<T, ?> column) {
        this.wrappers.add(buildOrderAsc(LambdaUtils.getFieldName(column)));
        return this;
    }

    @Override
    public LambdaQueryAdepts<T> orderByDesc(FieldGetter<T, ?> column) {
        this.wrappers.add(buildOrderDesc(LambdaUtils.getFieldName(column)));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> update(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildUpdate(LambdaUtils.getFieldName(column), value));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> unUpdate(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildUnUpdate(LambdaUtils.getFieldName(column), value));
        return this;
    }

    @Override
    public LambdaQueryAdepts<T> search(String value) {
        this.setTextSearch(value);
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> push(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildPush(LambdaUtils.getFieldName(column), value));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> pull(FieldGetter<T, ?> column, Object value) {
        this.wrappers.add(buildPull(LambdaUtils.getFieldName(column), value));
        return this;
    }

    public LambdaQueryAdepts<T> or(LambdaQueryAdepts<T> wrapper) {
        this.wrappers.add(new Wrapper(Option.OR, wrapper));
        return this;
    }


    @Override
    public LambdaQueryAdepts<T> inc(FieldGetter<T, ?> column, Long number) {
        this.wrappers.add(buildInc(LambdaUtils.getFieldName(column), number));
        return this;
    }
}
