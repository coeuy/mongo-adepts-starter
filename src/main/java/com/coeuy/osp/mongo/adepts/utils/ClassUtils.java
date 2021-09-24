package com.coeuy.osp.mongo.adepts.utils;

import com.coeuy.osp.mongo.adepts.constants.BasicType;
import com.google.common.collect.Lists;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class ClassUtils {


    /**
     * 递归获取类字段（包含父类）
     * @param clazz 类
     * @param fields 字段
     * @param <T> 类型
     * @return 字段列表
     */
    public static <T> List<Field> getClassFields(Class<? super T> clazz, List<Field> fields){
        if (Objects.nonNull(clazz)){
            fields.addAll(Lists.newArrayList(clazz.getDeclaredFields()));
            return getClassFields(clazz.getSuperclass(),fields);
        }
        return fields;
    }

    public static boolean isAllAssignableFrom(Class<?>[] types1, Class<?>[] types2) {
        if (ArrayUtil.isEmpty(types1) && ArrayUtil.isEmpty(types2)) {
            return true;
        }
        if (null == types1 || null == types2) {
            // 任何一个为null不相等（之前已判断两个都为null的情况）
            return false;
        }
        if (types1.length != types2.length) {
            return false;
        }

        Class<?> type1;
        Class<?> type2;
        for (int i = 0; i < types1.length; i++) {
            type1 = types1[i];
            type2 = types2[i];
            if (isBasicType(type1) && isBasicType(type2)) {
                // 原始类型和包装类型存在不一致情况
                if (BasicType.unWrap(type1) != BasicType.unWrap(type2)) {
                    return false;
                }
            } else if (!type1.isAssignableFrom(type2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isBasicType(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        if (null == clazz) {
            return false;
        }
        return BasicType.WRAPPER_PRIMITIVE_MAP.containsKey(clazz);
    }

}
