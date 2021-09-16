package com.coeuy.osp.mongo.adepts.model.next;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coeuy.osp.mongo.adepts.utils.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * <p>
 * 向下查询
 * </p>
 *
 * @author yarnk
 * @date 2021/9/16
 */
@Slf4j
@Data
public class NextResult<T> implements Serializable {

    private static final long serialVersionUID = 3984564231508440455L;
    private List<T> records;

    private long size;

    private String sortKey;
    private Object startValue;


    private Object endValue;

    public NextResult(List<T> records, long size, String sortKey, Object startValue, Object endValue) {
        this.records = records;
        this.size = size;
        this.sortKey = sortKey;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    public static <T> NextResult<T> result(List<T> list, NextInfo nextInfo) {

        return new NextResult<T>(list,list.size(),nextInfo.getSortKey(),nextInfo.getStartValue(),getEndValue(list));
    }

    public static <T> NextResult<T> result(List<T> list, Object startValue) {
        Map<String, Object> endValue = getEndValue(list);
        String key = null;
        Object val = null;
        for (String s : endValue.keySet()) {
            key = s;
            val = endValue.get(s);
        }
        return new NextResult<T>(list,list.size(),key,startValue,val);
    }

    @SuppressWarnings("all")
    /**
     * 获取加了@Id 的字段名
     */
    public static <T> Map<String,Object> getEndValue(List<T> list){
        Object endValue = null;
        String sortKey = "";
        if (CollectionUtils.isNotEmpty(list)){
            try{
                T t = list.get(list.size() - 1);
                Class<T> tClass = (Class<T>) t.getClass();
                List<Field> fields = getSuperClassFields(tClass, Lists.newArrayList());
                for (Field field : fields) {
                    Id annotation = field.getAnnotation(Id.class);
                    if (Objects.nonNull(annotation)){
                        sortKey = field.getName();
                        continue;
                    }
                }
                Object json = JSON.toJSON(t);
                String jsonString = JSON.toJSONString(json);
                JSONObject object = JSON.parseObject(jsonString);
                endValue = object.get(sortKey);
            }catch (Exception e){
                log.error("获取EndValue异常",e);
            }
        }
        HashMap<String, Object> hashMap = Maps.newHashMap();
        hashMap.put(sortKey,endValue);
        return hashMap;
    }

    /**
     * 递归获取父类字段
     * @param clazz 类
     * @param fields 字段
     * @param <T> 类型
     * @return 字段列表
     */
    private static <T>List<Field>  getSuperClassFields(Class<? super T> clazz,List<Field> fields){
        if (Objects.nonNull(clazz)){
            fields.addAll(Lists.newArrayList(clazz.getDeclaredFields()));
            return getSuperClassFields(clazz.getSuperclass(),fields);
        }
        return fields;
    }
}
