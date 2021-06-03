package com.coeuy.osp.mongo.adepts.utils;

import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.util.Set;

/**
 * Bean 处理工具
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/4/11 15:04
 */
@Slf4j
@Configuration
public class PackageUtils implements ResourceLoaderAware {
    public final static String DOT = ".";
    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

    public static Set<String> scanner(String packageUrl) {
        try {
            return new PackageUtils().doScan(packageUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBeanName(String className) {
        if (!className.contains(DOT)) {
            log.error("获取Bean名称PARAM类名称错误");
            return "";
        }
        String beanNameBig = className.substring(className.lastIndexOf(DOT) + 1);
        char temp = beanNameBig.charAt(0);
        temp = (char) (temp + 32);
        return temp + beanNameBig.substring(1);
    }

    public Set<String> doScan(String scanPath) throws IOException {
        Set<String> classes = Sets.newHashSet();

        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                .concat(ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(scanPath))
                        .concat("/**/*.class"));
        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        MetadataReader metadataReader = null;
        for (Resource resource : resources) {
            if (resource.isReadable()) {
                metadataReader = metadataReaderFactory.getMetadataReader(resource);
                try {
                    // 当类型不为抽象类或接口在添加到集合
                    if (metadataReader.getClassMetadata().isConcrete()) {
                        classes.add(Class.forName(metadataReader.getClassMetadata().getClassName()).getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return classes;
    }

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }
}
