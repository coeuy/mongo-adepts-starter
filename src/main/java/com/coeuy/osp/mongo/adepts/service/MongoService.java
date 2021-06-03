package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryAdepts;
import com.coeuy.osp.mongo.adepts.utils.ReflectionKit;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Mongo Adepts
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/16 10:09
 */
@Slf4j
public class MongoService<T> {
    protected Class<T> entityClass = currentModelClass();

    @SuppressWarnings("all")
    @Autowired(required = false)
    protected  MongoAdepts mongoAdepts;

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 0);
    }

    public T getOne(QueryAdepts queryAdepts) {
        return mongoAdepts.getOne(queryAdepts, entityClass);
    }

    public T getById(Serializable id) {
        return mongoAdepts.getById(id, entityClass);
    }

    public List<T> list() {
        return mongoAdepts.list(entityClass);
    }

    public List<T> list(QueryAdepts queryAdepts) {
        return mongoAdepts.list(queryAdepts, entityClass);
    }

    public List<T> listByIds(Collection<? extends Serializable> idList) {
        Iterable<T> allByIds = mongoAdepts.listByIds(idList, entityClass);
        return Lists.newArrayList(allByIds);
    }

    public PageResult<T> page(PageInfo pageInfo) {
        return mongoAdepts.page(pageInfo, entityClass);
    }

    public PageResult<T> page(PageInfo pageInfo, QueryAdepts queryAdepts) {
        return mongoAdepts.page(pageInfo, queryAdepts, entityClass);
    }


    public T insert(T t) {
        return mongoAdepts.insert(t);
    }

    public boolean insertBatch(Collection<T> entityList) {
        return mongoAdepts.insertBatch(entityList, entityClass);
    }

    public T save(T t) {
        return mongoAdepts.save(t);
    }

    public boolean update(QueryAdepts queryAdepts) {
        return mongoAdepts.update(queryAdepts, entityClass);
    }

    public T findAndModify(QueryAdepts queryAdepts) {
        return mongoAdepts.findAndModify(queryAdepts, entityClass);
    }

    public boolean updateMulti(QueryAdepts queryAdepts) {
        return mongoAdepts.updateMulti(queryAdepts, entityClass);
    }

    public boolean delete(QueryAdepts queryAdepts) {
        return mongoAdepts.delete(queryAdepts, entityClass);
    }

    public boolean delete(T t) {
        return mongoAdepts.delete(t, entityClass);
    }

    public int count(QueryAdepts queryAdepts) {
        return mongoAdepts.count(queryAdepts, entityClass);
    }

    public boolean removeAll() {
        return mongoAdepts.deleteAll(entityClass);
    }

    public boolean exists(QueryAdepts queryAdepts) {
        return mongoAdepts.exists(queryAdepts, entityClass);
    }

    public boolean collectionExists() {
        return mongoAdepts.exists(entityClass);
    }

    public List<T> group(QueryAdepts queryAdepts, String... keys) {
        return mongoAdepts.group(queryAdepts, entityClass, keys);
    }

    public PageResult<T> group(PageInfo pageInfo, QueryAdepts queryAdepts, String... keys) {
        return mongoAdepts.group(pageInfo, queryAdepts, entityClass, keys);
    }


}
