package com.coeuy.osp.mongo.adepts.config;

import com.coeuy.osp.mongo.adepts.handler.QueryHandler;
import com.coeuy.osp.mongo.adepts.handler.UpdateHandler;
import com.coeuy.osp.mongo.adepts.handler.WrapperHandler;
import com.coeuy.osp.mongo.adepts.service.MongoAdepts;
import com.mongodb.lang.NonNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.MongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.MongoPersistentProperty;

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
@Configuration
@RequiredArgsConstructor
public class MongoAdeptsConfiguration {

    private final MongoTemplate mongoTemplate;

    private final MongoAdeptsProperties properties;

    private final @NonNull
    MappingContext<? extends MongoPersistentEntity<?>, MongoPersistentProperty> context;

    @Bean
    @Primary
    public MongoAdepts mongoAdepts() {
        if (properties.isDebug()) {
            log.info("Init bean MongoAdepts···");
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
