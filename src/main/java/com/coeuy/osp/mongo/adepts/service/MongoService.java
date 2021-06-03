package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import com.coeuy.osp.mongo.adepts.utils.ReflectionKit;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
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

    @Autowired(required = false)
    protected  MongoAdepts mongoAdepts;

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
    }

    public T getOne(QueryWrapper queryWrapper) {
        return mongoAdepts.getOne(queryWrapper, entityClass);
    }

    public T getById(Serializable id) {
        return mongoAdepts.getById(id, entityClass);
    }

    public List<T> list() {
        return mongoAdepts.list(entityClass);
    }

    public List<T> list(QueryWrapper queryWrapper) {
        return mongoAdepts.list(queryWrapper, entityClass);
    }

    public List<T> listByIds(Collection<? extends Serializable> idList) {
        Iterable<T> allByIds = mongoAdepts.listByIds(idList, entityClass);
        return Lists.newArrayList(allByIds);
    }

    public PageResult<T> page(PageInfo pageInfo) {
        return mongoAdepts.page(pageInfo, entityClass);
    }

    public PageResult<T> page(PageInfo pageInfo, QueryWrapper queryWrapper) {
        return mongoAdepts.page(pageInfo, queryWrapper, entityClass);
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

    public boolean update(QueryWrapper queryWrapper) {
        return mongoAdepts.update(queryWrapper, entityClass);
    }

    public T findAndModify(QueryWrapper queryWrapper) {
        return mongoAdepts.findAndModify(queryWrapper, entityClass);
    }

    public boolean updateMulti(QueryWrapper queryWrapper) {
        return mongoAdepts.updateMulti(queryWrapper, entityClass);
    }

    public boolean delete(QueryWrapper queryWrapper) {
        return mongoAdepts.delete(queryWrapper, entityClass);
    }

    public boolean delete(T t) {
        return mongoAdepts.delete(t, entityClass);
    }

    public int count(QueryWrapper queryWrapper) {
        return mongoAdepts.count(queryWrapper, entityClass);
    }

    public boolean removeAll() {
        return mongoAdepts.deleteAll(entityClass);
    }

    public boolean exists(QueryWrapper queryWrapper) {
        return mongoAdepts.exists(queryWrapper, entityClass);
    }

    public boolean collectionExists() {
        return mongoAdepts.exists(entityClass);
    }

    public List<T> group(QueryWrapper queryWrapper, String... keys) {
        return mongoAdepts.group(queryWrapper, entityClass, keys);
    }

    public PageResult<T> group(PageInfo pageInfo, QueryWrapper queryWrapper, String... keys) {
        return mongoAdepts.group(pageInfo, queryWrapper, entityClass, keys);
    }


}
