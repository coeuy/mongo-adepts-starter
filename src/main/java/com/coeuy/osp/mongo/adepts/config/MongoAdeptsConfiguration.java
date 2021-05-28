package com.coeuy.osp.mongo.adepts.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/6/17 11:17
 */
@Data
@Slf4j
@Configuration
@ComponentScan({"com.coeuy.osp.mongo"})
@ConfigurationProperties(prefix = "mongo-adepts")
public class MongoAdeptsConfiguration {
    /**
     * 是否开启debug模式
     */
    private boolean debug;

    public MongoAdeptsConfiguration() {
        System.out.println(".    .                         .       .          .      \n" +
                "|\\  /|                        / \\      |         _|_     \n" +
                "| \\/ | .-. .--. .-.. .-.     /___\\  .-.| .-. .,-. |  .--.\n" +
                "|    |(   )|  |(   |(   )   /     \\(   |(.-' |   )|  `--.\n" +
                "'    ' `-' '  `-`-`| `-'   '       ``-'`-`--'|`-' `-'`--'\n" +
                "                ._.'                         |           \n" +
                "                                             '           \n"+
                "\n version:");
    }
}
