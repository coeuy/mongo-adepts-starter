package com.coeuy.osp.mongo.adepts.utils;

import com.coeuy.osp.mongo.adepts.config.FieldGetter;
import com.coeuy.osp.mongo.adepts.support.LambdaMeta;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author yarnk
 * @date 2021/9/9
 */
public class LambdaUtils {

    /**
     * 获取字段名  支持
     *
     * @return
     */
    public static <T> String getFieldName(FieldGetter<T, ?> fn)  {

        // 从lambda信息取出method、field、class等
        String fieldName = getSerializedLambda(fn).getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        return fieldName;
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
        return new LambdaMeta(aClass,fieldName);
    }


}