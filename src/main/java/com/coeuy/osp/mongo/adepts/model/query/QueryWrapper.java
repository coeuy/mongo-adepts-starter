package com.coeuy.osp.mongo.adepts.model.query;

import com.coeuy.osp.mongo.adepts.constants.Option;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 查询包装器·基础
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QueryWrapper extends WrapperBuilder implements Serializable {

    private static final long serialVersionUID = -2523209411758953004L;
    /**
     * 防止多次比较
     */
    protected boolean compared;

    /**
     * 条件集合
     */
    protected List<Wrapper> wrappers = new ArrayList<>();

    /**
     * 全文索引
     */
    protected String textSearch;

    private Class<?> entityClass;


    public QueryWrapper eq(String key, Object value) {
        this.wrappers.add(buildEq(key, value));
        return this;
    }



    public QueryWrapper ne(String key, Object value) {
        this.wrappers.add(buildNe(key, value));
        return this;
    }


    public QueryWrapper in(String key, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildIn(key, value));
        }
        return this;
    }


    public QueryWrapper ge(String key, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildGe(key, value));
        }
        return this;
    }


    public QueryWrapper gt(String key, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildGt(key, value));
        }
        return this;
    }


    public QueryWrapper le(String key, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildLe(key, value));
        }
        return this;
    }

    public QueryWrapper lt(String key, Object value) {
        if (!compared) {
            compared = true;
            this.wrappers.add(buildLt(key, value));
        }
        return this;
    }

    public QueryWrapper scope(String key, Object ge, Object le) {
        this.wrappers.add(new Wrapper(Option.SCOPE, key, ge, le));
        return this;
    }


    public QueryWrapper like(String key, CharSequence keyword) {
        this.wrappers.add(buildLike(key,keyword));
        return this;
    }


    public QueryWrapper likeLeft(String key, CharSequence keyword) {
        this.wrappers.add(buildLikeLeft(key,keyword));
        return this;
    }


    public QueryWrapper likeRight(String key, CharSequence keyword) {
        this.wrappers.add(buildLikeLeft(key,keyword));
        return this;
    }


    public QueryWrapper orderByAsc(String key) {
        this.wrappers.add(buildOrderAsc(key));
        return this;
    }


    public QueryWrapper orderByDesc(String key) {
        this.wrappers.add(buildOrderDesc(key));
        return this;
    }


    public QueryWrapper update(String key, Object value) {
        this.wrappers.add(buildUpdate(key, value));
        return this;
    }


    public QueryWrapper unUpdate(String key, Object value) {
        this.wrappers.add(buildUnUpdate(key,value));
        return this;
    }

    public QueryWrapper search(String value) {
        this.setTextSearch(value);
        return this;
    }


    public QueryWrapper push(String key, Object value) {
        this.wrappers.add(buildPush(key, value));
        return this;
    }


    public QueryWrapper pull(String key, Object value) {
        this.wrappers.add(buildPull(key, value));
        return this;
    }


    public QueryWrapper or(QueryWrapper wrapper) {
        this.wrappers.add(new Wrapper(Option.OR, wrapper));
        return this;
    }


    public QueryWrapper inc(String key, Integer number) {
        this.wrappers.add(buildInc(key,number));
        return this;
    }
}
