package com.coeuy.osp.mongo.adepts.utils;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.config.Func0;
import com.coeuy.osp.mongo.adepts.support.LambdaMeta;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * Lambda链式调用获取字段名工具
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class LambdaUtils {

    /**
     * 根据方法获取字段名
     * （如果字段有Field注解则使用注解中的值）
     *
     * @return fieldName
     */
    public static <T> String getFieldName(FieldGetter<T, ?> fn)  {
        // 从lambda信息取出method、field、class等
        SerializedLambda serializedLambda = getSerializedLambda(fn);
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        fieldName = getMongoField(fieldName,serializedLambda);
        return fieldName;
    }


    private static String getMongoField(String originalFieldName, SerializedLambda serializedLambda){
        Field field;
        try {
            field = Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(originalFieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        // 如果字段上有注解则获取注解上的 value 值
        org.springframework.data.mongodb.core.mapping.Field documentField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
        if (documentField != null && documentField.value().length() > 0) {
            originalFieldName = documentField.value();
        }
        return originalFieldName;
    }

    public static <T>SerializedLambda getSerializedLambda(FieldGetter<T, ?> fn){
        // 从function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = fn.getClass().getDeclaredMethod("writeReplace");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 从序列化方法取出序列化的lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(fn);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);
        return serializedLambda;
    }

    public static <T> LambdaMeta getMeta(FieldGetter<T, ?> fn)  {
        SerializedLambda serializedLambda = getSerializedLambda(fn);
        System.out.println(serializedLambda);
        String implClass = serializedLambda.getImplClass();
        implClass= implClass.replace("/",".");
        System.out.println(implClass);
        Class<?> aClass;
        try{
            aClass = Class.forName(implClass);
        }catch (Exception e){
            aClass = null;
            e.printStackTrace();
        }
        // 从lambda信息取出method、field、class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        fieldName = getMongoField(fieldName, serializedLambda);
        return new LambdaMeta(aClass,fieldName);
    }

    /**
     * 获取原本的字段名
     * @param fn 方法
     * @param <T> 类型
     * @return 原名
     */
    public static <T>String getOriginalField(FieldGetter<T, ?> fn){
        String fieldName = getSerializedLambda(fn).getImplMethodName().substring("get".length());
        return fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
    }
}
