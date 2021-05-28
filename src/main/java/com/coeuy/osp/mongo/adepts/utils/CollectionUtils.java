package com.coeuy.osp.mongo.adepts.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;


/**
 * <p> 集合工具类 </p>
 *
 * @author Yarnk .  yarnk@coeuy.com
 * @date 2020/5/18 7:51 下午
 */
public class CollectionUtils {

    /**
     * 校验集合是否为空
     *
     * @param collection 入参
     * @return boolean
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 校验集合是否不为空
     *
     * @param collection 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断Map是否为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }

    /**
     * 判断Map是否不为空
     *
     * @param map 入参
     * @return boolean
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }


    /**
     * 找出两个集合中相同的元素
     *
     * @param coll1
     * @param coll2
     * @return
     */
    public static <T> Collection<T> getSame(Collection<T> coll1, Collection<T> coll2) {
        //使用LinkedList防止差异过大时,元素拷贝
        Collection<T> csReturn = new LinkedList<>();
        Collection<T> max = coll1;
        Collection<T> min = coll2;
        //先比较大小,这样会减少后续map的if判断次数
        if (coll1.size() < coll2.size()) {
            max = coll2;
            min = coll1;
        }
        //直接指定大小,防止再散列
        Map<Object, Integer> map = new HashMap<>(max.size());
        for (Object object : max) {
            map.put(object, 1);
        }
        for (T t : min) {
            if (map.get(t) != null) {
                csReturn.add(t);
            }
        }
        return csReturn;
    }

}
