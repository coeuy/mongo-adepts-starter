package com.coeuy.osp.mongo.adepts.utils;

import java.lang.reflect.Array;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class ArrayUtil {

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static boolean isEmpty(Object array) {
        if (array != null) {
            if (isArray(array)) {
                return 0 == Array.getLength(array);
            }
            return false;
        }
        return true;
    }

    @SafeVarargs
    public static <T> T[] append(T[] buffer, T... newElements) {
        if (isEmpty(buffer)) {
            return newElements;
        }
        return insert(buffer, buffer.length, newElements);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] insert(T[] buffer, int index, T... newElements) {
        return (T[]) insert((T[]) buffer, index, newElements);
    }

    // ---------------------------------------------------------------------- isNotEmpty

    /**
     * 数组是否为非空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为非空
     */
    public static <T> boolean isNotEmpty(T[] array) {
        return (null != array && array.length != 0);
    }
}
