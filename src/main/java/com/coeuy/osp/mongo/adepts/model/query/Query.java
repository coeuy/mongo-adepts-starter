package com.coeuy.osp.mongo.adepts.model.query;

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

    QueryWrapper eq(R column, Object value);

    QueryWrapper ne(R column, Object value);

    QueryWrapper in(R column, Object value);

    QueryWrapper ge(R column, Object value);

    QueryWrapper gt(R column, Object value);

    QueryWrapper le(R column, Object value);

    QueryWrapper lt(R column, Object value);

    QueryWrapper geAndLe(R column, Object ge, Object le);

    QueryWrapper like(R column, CharSequence keyword);

    QueryWrapper likeLeft(R column, CharSequence keyword);

    QueryWrapper likeRight(R column, CharSequence keyword);

    QueryWrapper orderByAsc(R column);

    QueryWrapper orderByDesc(R column);

    QueryWrapper update(R column, Object value);

    QueryWrapper unUpdate(R column, Object value);

    QueryWrapper search(String value);

    QueryWrapper push(R column, Object value);

    QueryWrapper pull(R column, Object value);

    QueryWrapper or(QueryWrapper wrapper);

    QueryWrapper inc(R column, Integer number);
}
