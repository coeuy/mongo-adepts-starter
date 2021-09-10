package com.coeuy.osp.mongo.adepts.support;

/**
 * <p>
 * Lambda 信息
 * </p>
 *
 * @author yarnk
 * @date 2021/9/10
 */
public class LambdaMeta {

    private final Class<?> entityClass;
    private final String  fieldName;

    public LambdaMeta(Class<?> entityClass, String fieldName) {
        this.entityClass = entityClass;
        this.fieldName = fieldName;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public String getFieldName() {
        return fieldName;
    }
}
