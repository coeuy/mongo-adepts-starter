package com.coeuy.osp.mongo.adepts.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * <p> 对象转换工具类 <p/>
 * Open source from coeuy.com
 *
 * @author Yarnk .  yarnk@coeuy.com
 */
@Slf4j
@Component
public class ConvertUtils {

    private static ObjectMapper objectMapper;

    /**
     * 对象转换
     *
     * @param fromValue   源对象
     * @param toValueType 目标对象
     * @param <T>         类型
     * @return T
     */
    public static <T> T convert(Object fromValue, Class<T> toValueType) {

        return objectMapper.convertValue(fromValue, toValueType);

    }

    public static <T> T convert(Class<T> toValueType) {
        return null;
    }

    /**
     * 将任意vo转化成map
     *
     * @param t Obj对象
     * @return result
     */
    public static <T> Map<String, Object> convertToMap(T t) {
        Map<String, Object> result = new HashMap<>(8);
        Method[] methods = t.getClass().getMethods();
        try {
            for (Method method : methods) {
                Class<?>[] paramClass = method.getParameterTypes();
                // 如果方法带参数，则跳过
                if (paramClass.length > 0) {
                    continue;
                }
                String methodName = method.getName();
                if (methodName.startsWith("get")) {
                    Object value = method.invoke(t);
                    result.put(methodName, value);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    @Resource
    public void setObjectMapper(ObjectMapper objectMapper) {
        ConvertUtils.objectMapper = objectMapper;
    }
}
