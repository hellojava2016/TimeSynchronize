/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: ConvertUtils.java 1211 2010-09-10 16:20:45Z calvinxiu $
 */
package cn.gov.cbrc.bankriskcontrol.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
@SuppressWarnings("rawtypes")
public class ConvertUtils {

	static {
		registerDateConverter();
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成List.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 */
	public static List<Object> convertElementPropertyToList(final Collection<Object> collection, final String propertyName) {
		List<Object> list = new ArrayList<Object>();

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}

		return list;
	}

	/**
	 * 提取集合中的对象的属性(通过getter函数), 组合成由分割符分隔的字符串.
	 * 
	 * @param collection 来源集合.
	 * @param propertyName 要提取的属性名.
	 * @param separator 分隔符.
	 */
	public static String convertElementPropertyToString(final Collection<Object> collection, final String propertyName,
			final String separator) {
		List<Object> list = convertElementPropertyToList(collection, propertyName);
		return StringUtils.join(list, separator);
	}

	/**
	 * 转换字符串到相应类型.
	 * 
	 * @param value 待转换的字符串.
	 * @param toType 转换目标类型.
	 */
	public static Object convertStringToObject(String value, Class<?> toType) {
		try {
			return org.apache.commons.beanutils.ConvertUtils.convert(value, toType);
		} catch (Exception e) {
			throw ReflectionUtils.convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 定义日期Converter的格式: yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss
	 */
	private static void registerDateConverter() {
		DateConverter dc = new DateConverter();
		dc.setUseLocaleFormat(true);
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","MM/dd/yyyy HH:mm" });
		org.apache.commons.beanutils.ConvertUtils.register(dc, Date.class);
	}
	
	/**
     * Google Collection的方法，方便构造一个可写的List
     * 
     * @param <E>
     * @param elements
     * @return
     */
    public static <E> List<E> newArrayList(E... elements) {
        ArrayList<E> list = new ArrayList<E>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }
    
    /**
     * 将Map的valueSet转换为排序过的列表
     * @param map
     * @return
     */
    public static List<Comparable> convertMapValuesToSortedList(Map map){
    	List<Comparable> list=new ArrayList<Comparable>();
    	for(Object object:map.values()){
    		list.add((Comparable)object);
    	}
    	Collections.sort(list);
    	return list;
    }
    
    public static int doubleToInt(double value){
    	return Integer.parseInt(new java.text.DecimalFormat("0").format(value));
    }
    
	/**
	 * 得到集合的某个属性值
	 * @param <T>
	 * @param <M>
	 * @param objectList
	 * @param propertyName
	 * @param m
	 * @return
	 */
	public static <T, M> List<M> getPropertyList(Collection<T> objectList, String propertyName, Class<M> m) {
        List<M> mlist = new ArrayList<M>();
        for (T t : objectList) {
            try {
                mlist.add((M) PropertyUtils.getProperty(t, propertyName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mlist;
    }
    
    /**
     * 得到以属性为Key的MAP
     * @param <T>
     * @param <M>
     * @param objectList
     * @param propertyName
     * @param m
     * @return
     */
    public static <T, M> Map<M, T> getMapByList(Collection<T> objectList, String propertyName, Class<M> m) {
        Map<M, T> map = new HashMap<M, T>();
        for (T t : objectList) {
            try {
                map.put((M) PropertyUtils.getProperty(t, propertyName), t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    
	public static <T> T getObjectFromList(Collection<T> objectList, String propertyName, Object value) {
		try {
			for (T t : objectList) {
				if (PropertyUtils.getProperty(t, propertyName).equals(value))
					return t;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
    
    /**
     * 将double转换为2位小数
     * @param value
     * @return
     */
    public static String get2pointDouble(double value){
    	if(Double.isNaN(value)||Double.isInfinite(value))
    		return "0.00";
    	return new DecimalFormat("0.00").format(value);
    }
    
    /**
     * 将List中的数据根据分隔符转换成字符串
     * 
     * @param <T>
     *            List中数据的类型，支持泛型
     * @param list
     *            要转换的数据
     * @param separator
     *            转换后数据间的分隔符
     * @return String 根据分隔符转换成的字符串
     */
    public static <T> String convertList2String(List<T> list, String separator) {
		StringBuilder builder = new StringBuilder();
		int size = list==null?0:list.size();
		for (int i = 0; i < size; i++) {
			T t = list.get(i);
			if (i == 0) {
				builder.append(String.valueOf(t));
			} else
				builder.append(separator).append(String.valueOf(t));

		}
		return builder.toString();
	}
}
