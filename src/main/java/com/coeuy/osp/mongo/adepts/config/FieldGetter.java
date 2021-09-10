package com.coeuy.osp.mongo.adepts.config;

import com.mongodb.Function;

import java.io.Serializable;


/**
 * <p>
 * 字段获取序列化对象
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
@SuppressWarnings("all")
@FunctionalInterface
public interface FieldGetter<T, R> extends Function<T, R>, Serializable {
}
