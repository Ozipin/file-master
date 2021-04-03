package com.ozipin.filemaster.utils;

import com.alibaba.excel.annotation.ExcelProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 反射工具类
 */
public class ReflectUtils {

    /**
     * 获取指定EXCEL实体类表头信息
     * @param clazz
     * @return
     */
    public static Map<Integer, String> getExcelHeadMap(Class clazz){
        Map<Integer, String> headMap = new HashMap<>();
        int i = 0;
        for (Field field : clazz.getDeclaredFields()) {
            ExcelProperty annotation = field.getAnnotation(ExcelProperty.class);
            if (!Objects.isNull(annotation)){
                headMap.put(i, annotation.value()[0]);
                i++;
            }
        }
        return headMap;
    }
}