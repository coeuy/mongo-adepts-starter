package com.coeuy.osp.mongo.adepts.utils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * Obj对象工具类
 *
 * @author Yarnk .  yarnk@coeuy.com
 */
public class ObjectUtils {

    public static boolean equal(Object obj1, Object obj2) {
        return Objects.equals(obj1, obj2);
    }

    public static boolean notEqual(Object obj1, Object obj2) {
        return !equal(obj1, obj2);
    }


    public static int length(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length();
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size();
        }

        int count;
        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            count = 0;
            while (iter.hasNext()) {
                count++;
                iter.next();
            }
            return count;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            count = 0;
            while (enumeration.hasMoreElements()) {
                count++;
                enumeration.nextElement();
            }
            return count;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        return -1;
    }

    public static boolean contains(Object obj, Object element) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof String) {
            if (element == null) {
                return false;
            }
            return ((String) obj).contains(element.toString());
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).contains(element);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).containsValue(element);
        }

        if (obj instanceof Iterator) {
            Iterator<?> iter = (Iterator<?>) obj;
            while (iter.hasNext()) {
                Object o = iter.next();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj instanceof Enumeration) {
            Enumeration<?> enumeration = (Enumeration<?>) obj;
            while (enumeration.hasMoreElements()) {
                Object o = enumeration.nextElement();
                if (equal(o, element)) {
                    return true;
                }
            }
            return false;
        }
        if (obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object o = Array.get(obj, i);
                if (equal(o, element)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static boolean isNull(Object obj) {
        return null == obj;
    }


    public static boolean isNotNull(Object obj) {
        //noinspection ConstantConditions
        return null != obj && !obj.equals(null);
    }


    /**
     * 如果给定对象为{@code null}返回默认值
     *
     * <pre>
     * ObjectUtil.defaultIfNull(null, null)      = null
     * ObjectUtil.defaultIfNull(null, "")        = ""
     * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
     * ObjectUtil.defaultIfNull("abc", *)        = "abc"
     * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
     * </pre>
     *
     * @param <T>          对象类型
     * @param object       被检查对象，可能为{@code null}
     * @param defaultValue 被检查对象为{@code null}返回的默认值，可以为{@code null}
     * @return 被检查对象为{@code null}返回默认值，否则返回原值
     * @since 3.0.7
     */
    public static <T> T defaultIfNull(final T object, final T defaultValue) {
        return (null != object) ? object : defaultValue;
    }

}
