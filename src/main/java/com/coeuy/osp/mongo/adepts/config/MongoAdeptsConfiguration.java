package com.coeuy.osp.mongo.adepts.config;

import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.handler.WrapperHandler;
import com.coeuy.osp.mongo.adepts.service.MongoAdepts;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

import java.util.Objects;

/**
 * <p>
 * 核心配置
 * </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/17 11:17
 */
@Data
@Slf4j
@Configuration(proxyBeanMethods = false)
public class MongoAdeptsConfiguration {

    @SuppressWarnings("all")
    @Autowired(required = false)
    private  MongoTemplate mongoTemplate;
    @SuppressWarnings("all")
    @Autowired(required = false)
    private  MongoAdeptsProperties properties;
    @SuppressWarnings("all")
    @Autowired(required = false)
    private MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context;

    @Bean
    public MongoAdepts mongoAdepts() {
        if (properties.isDebug()) {
            log.info("Init bean MongoAdepts···");
        }
        if (Objects.isNull(mongoTemplate)||Objects.isNull(context)){
            log.info("No mongo data dependency in your project,The mongo adepts just start of static mode now. ");
            return null;
        }
        return new MongoAdepts(
                mongoTemplate,
                new QueryHandler(properties),
                new UpdateHandler(properties, new WrapperHandler()),
                properties,
                context
        );
    }
}
