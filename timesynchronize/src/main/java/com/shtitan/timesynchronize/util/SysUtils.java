package com.shtitan.timesynchronize.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public class SysUtils {
	 private static final Random random = new Random();
	 
	    
	    /**
	     * 判断一个字符串是不是整数
	     * @param digitStr 需要判断的字符串
	     * @param length 字符串长度
	     * @return
	     */
	    public static boolean isDigit(String digitStr, int length){
	        Pattern macPattern = Pattern.compile("^[0-9]\\d*$");
	        Matcher macMatcher = macPattern.matcher(digitStr);
	        if(macMatcher.find()){
	        	if(length > 0){
	        		if(digitStr.length() == length){
	        			return true;
	        		}else{
	        			return false;
	        		}
	        	}
	            return true;
	        }
	        return false;
	    }
	 
	 
	 
	 public static String getRandomString(int length) { //length表示生成字符串的长度  
		    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
		    Random random = new Random();     
		    StringBuffer sb = new StringBuffer();     
		    for (int i = 0; i < length; i++) {     
		        int number = random.nextInt(base.length());     
		        sb.append(base.charAt(number));     
		    }     
		    return sb.toString();     
		 } 
	 
	    /**
	     *  生成长度为length的字符串,字符串只包含数字
	     * @param length 字符串的长度
	     * @return
	     */
	    public static String buildRandom (final int length) {
	        //长度为length的最多整数
	        String maxStr = StringUtils.rightPad("1", length + 1, '0');
	        long max = Long.parseLong(maxStr);
	        long i = random.nextLong();  //取得随机数
	        i = Math.abs(i) % max;  //取正数，并限制其长度
	        String value = StringUtils.leftPad(String.valueOf(i), length, '0');
	        return value;
	    }
	    
	    
	    public static void main(String[] args){
	    	try{
	    		throw new NullPointerException();
	    	}catch(Exception e){
	    		System.out.println(1);
	    	}
	    	System.out.println(buildRandom(10));
	    	System.out.println(new Date(1440432000000L));
	    }
	    
	    /**
	     * 根据条件生成cronTrigger表达式
	     * @param timeDate 时间参数 格式HH:mm:ss
	     * @param intervalType 0:每天;1:每周：2：每月
	     * @return
	     */
	    public static String getCronTriggerExpr(Date timeDate,int intervalType, int days){
	        String cronTriggerExpr = DateUtil.getDateTimeCronTriggerExpr(timeDate, 0);
	        if(intervalType == 0){
	            cronTriggerExpr += " * * ?";
	        }else if(intervalType == 1){
	        	cronTriggerExpr += " ? * " + days;
	        }else{
	        	cronTriggerExpr +=  " "+ days + " * ?";
	        }
	        return cronTriggerExpr;
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
		public static <T, M> List<M> getPropertyList(List<T> objectList, String propertyName, Class<M> m) {
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
		
		
		
		public static <T> List<T> getSubList(List<T> list,int pageNo,int pageSize){
			int size=list.size();
			int firstIndex=(pageNo-1)*pageSize;
			int lastIndex=pageNo*pageSize;
			List<T> result=new ArrayList<T>();
			if(lastIndex<size){
				for(int i=firstIndex;i<lastIndex;i++){
					result.add(list.get(i));
				}
			}else{
				for(int i=firstIndex;i<size;i++){
					result.add(list.get(i));
				}
			}
			return result;
		}
		
		public static Map toMap(int keys[],int values[]){
			Map map=new HashMap();
	        for(int i=0;i<keys.length;i++){
	        	map.put(keys[i], values[i]);
	        }
	        return map;
		}
		
	/**
	 * 确定逗号隔开的字符串是否有交集 例如a,b,c和b,4,r有交集为4
	 * @param src
	 * @param desc
	 * @return
	 */
	public static boolean isCommaStringEqual(String src, String desc) {
		if (StringUtils.isBlank(src) || StringUtils.isBlank(desc))
			return false;
		String[] src1 = src.split(",");
		String[] desc1 = desc.split(",");
		for (String temp : src1) {
			if (ArrayUtils.contains(desc1, temp))
				return true;
		}
		return false;
	}
	
	/**
	 * 将武汉,成都 改为"武汉" OR "成都"
	 * @param keywords
	 * @return
	 */
	public static String getSolrString(String keywords) {
		String[] keys = keywords.split(",");
		List<String> list = new ArrayList<String>();
		for (String key : keys) {
			list.add("\"" + key + "\"");
		}
		return ConvertUtils.convertList2String(list, "OR");
	}
	
	
	public static boolean isMobileNO(String mobiles){  
		  String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$"; 
		  Pattern p = Pattern.compile(regExp);    
		  Matcher m = p.matcher(mobiles);
		  return m.find(); 
	}
}
