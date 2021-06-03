package com.coeuy.osp.mongo.adepts.service;

import com.coeuy.osp.mongo.adepts.config.MongoAdeptsProperties;
import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;


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

}
