package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.exception.MongoAdeptsException;
import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.utils.ArrayUtil;
import com.coeuy.osp.mongo.adepts.utils.ClassUtils;
import com.coeuy.osp.mongo.adepts.utils.CollectionUtils;
import com.coeuy.osp.mongo.adepts.utils.LambdaUtils;
import com.google.common.collect.Lists;
import com.mongodb.MongoException;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.annotation.Id;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    public <T> boolean updateById(T t) {
        return updateById(t,false);
    }

    @Override
    public <T> boolean updateByIdSkipNull(T t) {
        return updateById(t,true);
    }

    @SuppressWarnings("unchecked")
    private <T> boolean updateById(T t,boolean skipNull){
        Class<T> aClass = (Class<T>) t.getClass();
        List<Field> classFields = ClassUtils.getClassFields(aClass, Lists.newArrayList());
        if (CollectionUtils.isEmpty(classFields)){
            log.warn("updateById:??????????????????");
            return false;
        }
        Update update = new Update();
        Object id = getIdValue(classFields,aClass);
        for (Field field : classFields) {
            String mongoField = field.getName();
            // ?????????????????? ??? serialVersionUID
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // ??????Field????????????value
            org.springframework.data.mongodb.core.mapping.Field annotation = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            if (annotation != null && annotation.value().length() > 0) {
                mongoField = annotation.value();
            }
            Object value;
            try {
                value = field.get(t);
                if (skipNull&&Objects.isNull(value)){
                    continue;
                }
            } catch (IllegalAccessException e) {
                log.error("????????????:{}?????????",field.getName());
                continue;
            }
            update.set(mongoField,value);
        }
        UpdateResult first = template().updateFirst(Query.query(Criteria.where("_id").is(id)), update, aClass);
        return first.getModifiedCount()>0;
    }

    @Override
    @SafeVarargs
    public final <T> boolean lambdaUpdateById(T t, FieldGetter<T, ?>... fieldGetters){
        if (ArrayUtil.isEmpty(fieldGetters)){
            return updateById(t,false);
        }
        return lambdaUpdateById(t,false,fieldGetters);
    }

    @Override
    @SafeVarargs
    public final <T> boolean lambdaUpdateByIdSkipNull(T t, FieldGetter<T, ?>... fieldGetters){
        if (ArrayUtil.isEmpty(fieldGetters)){
            return updateById(t,true);
        }
        return lambdaUpdateById(t,true,fieldGetters);
    }



    @SuppressWarnings("unchecked")
    private  <T> boolean lambdaUpdateById(T t,boolean skipNull,  FieldGetter<T, ?> ... fieldGetters) {
        Class<T> aClass = (Class<T>) t.getClass();
        List<Field> classFields = ClassUtils.getClassFields(aClass, Lists.newArrayList());
        if (CollectionUtils.isEmpty(classFields)){
            log.warn("lambdaUpdateById:??????????????????");
            return false;
        }
        Object id = getIdValue(classFields,aClass);

        Update update = new Update();
        // ?????? T ???????????????
        for (Field field : classFields) {
            // ?????????????????? ??? serialVersionUID
            if( Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            // ?????????????????? lambda ??????
            for (FieldGetter<T, ?> fieldGetter : fieldGetters) {
                // ???????????????????????????
                Object value;
                try {
                    value = field.get(t);
                    if (Objects.isNull(value)){
                        continue;
                    }
                } catch (IllegalAccessException e) {
                    log.error("????????????:{}?????????",field.getName());
                    continue;
                }
                // ????????????????????????????????????@Field?????????
                String originalField = LambdaUtils.getOriginalField(fieldGetter);
                // ??????????????????????????????
                if (field.getName().equals(originalField)){
                    update.set(LambdaUtils.getFieldName(fieldGetter),value);
                }
            }
        }
        UpdateResult first = template().updateFirst(Query.query(Criteria.where("_id").is(id)), update, aClass);
        return first.getModifiedCount()>0;
    }
    private <T>Object getIdValue(List<Field> classFields,T t){
        for (Field field : classFields) {
            Id annotation = field.getAnnotation(Id.class);
            if (Objects.nonNull(annotation)){
                try {
                    return field.get(t);
                } catch (IllegalAccessException e) {
                    log.error("??????ID???????????????",e);
                    throw new MongoAdeptsException("??????ID????????????");
                }
            }
        }
        throw new MongoAdeptsException("ID?????????");
    }
}
