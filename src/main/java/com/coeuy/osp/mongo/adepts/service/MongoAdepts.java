package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.model.page.PageInfo;
import com.coeuy.osp.mongo.adepts.model.page.PageResult;
import com.coeuy.osp.mongo.adepts.model.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.stereotype.Component;

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
public class MongoAdepts extends AbstractAdepts{

    public MongoAdepts(MongoTemplate mongoTemplate, QueryHandler queryHandler, UpdateHandler updateHandler, MongoAdeptsProperties properties, MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context) {
        super(mongoTemplate, queryHandler, updateHandler, properties, context);
    }

    @Override
    public String getCollectionName(Class<?> entityClass) {
        return super.getCollectionName(entityClass);
    }

    @Override
    public String determineCollectionName(Class<?> entityClass) {
        return super.determineCollectionName(entityClass);
    }

    @Override
    public <T> T getOne(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.getOne(queryWrapper, entityClass);
    }

    @Override
    public <T> T getById(Serializable id, Class<T> entityClass) {
        return super.getById(id, entityClass);
    }

    @Override
    public <T> List<T> list(Class<T> entityClass) {
        return super.list(entityClass);
    }

    @Override
    public <T> List<T> list(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.list(queryWrapper, entityClass);
    }

    @Override
    public <T> List<T> listByIds(Collection<? extends Serializable> idList, Class<T> entityClass) {
        return super.listByIds(idList, entityClass);
    }

    @Override
    public <T> PageResult<T> page(PageInfo pageInfo, Class<T> entityClass) {
        return super.page(pageInfo, entityClass);
    }

    @Override
    public <T> PageResult<T> page(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.page(pageInfo, queryWrapper, entityClass);
    }

    @Override
    public <T> T insert(T entity) {
        return super.insert(entity);
    }

    @Override
    public <T> boolean insertBatch(Collection<T> entityList, Class<T> entityClass) {
        return super.insertBatch(entityList, entityClass);
    }

    @Override
    public <T> T save(T entity) {
        return super.save(entity);
    }

    @Override
    public <T> boolean update(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.update(queryWrapper, entityClass);
    }

    @Override
    public <T> T findAndModify(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.findAndModify(queryWrapper, entityClass);
    }

    @Override
    public <T> boolean updateMulti(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.updateMulti(queryWrapper, entityClass);
    }

    @Override
    public <T> boolean delete(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.delete(queryWrapper, entityClass);
    }

    @Override
    public <T> boolean delete(T t, Class<T> entityClass) {
        return super.delete(t, entityClass);
    }

    @Override
    public <T> int count(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.count(queryWrapper, entityClass);
    }

    @Override
    public <T> boolean deleteAll(Class<T> entityClass) {
        return super.deleteAll(entityClass);
    }

    @Override
    public <T> boolean exists(QueryWrapper queryWrapper, Class<T> entityClass) {
        return super.exists(queryWrapper, entityClass);
    }

    @Override
    public <T> boolean exists(Class<T> entityClass) {
        return super.exists(entityClass);
    }

    @Override
    public <T> List<T> group(QueryWrapper queryWrapper, Class<T> entityClass, String... keys) {
        return super.group(queryWrapper, entityClass, keys);
    }

    @Override
    public <T> PageResult<T> group(PageInfo pageInfo, QueryWrapper queryWrapper, Class<T> entityClass, String... keys) {
        return super.group(pageInfo, queryWrapper, entityClass, keys);
    }
}
