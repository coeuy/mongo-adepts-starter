package com.coeuy.osp.mongo.adepts.utils;

import com.mongodb.Function;

import java.io.Serializable;


/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
@FunctionalInterface
public interface FieldGetter<T, R> extends Function<T, R>, Serializable {
}
