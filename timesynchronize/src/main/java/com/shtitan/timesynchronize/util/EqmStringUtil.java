package com.shtitan.timesynchronize.util;

import org.apache.commons.lang.StringUtils;

public class EqmStringUtil {
	 /**
     * 将value转化为length 长度的String
     * 
     * @param value
     * @param length
     * @return
     */
    public static String toString(Long value, int length) {
        String strValue = value.toString();
        StringBuilder sb = new StringBuilder();
        if (strValue.length() < length)
            for (int i = 0; i < length - strValue.length(); i++)
                sb.append("0");
        sb.append(strValue);
        return sb.toString();

    }
    
    
    public static String toString(Integer value, int length) {
        String strValue = value.toString();
        StringBuilder sb = new StringBuilder();
        if (strValue.length() < length)
            for (int i = 0; i < length - strValue.length(); i++)
                sb.append("0");
        sb.append(strValue);
        return sb.toString();

    }

    /**
     * 字符串转换方法
     * 
     * @param name
     *            需要转换的字符串
     * @return String 转换后的字符串
     * @author aaron lee
     */
    public static String capitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    /**
     * 计算两个字符串相同部分的长度（从左至右）
     * 
     * @param src
     * @param dst
     * @return
     */
    public static int getSameStrLen(String src, String dst) {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(dst))
            return -1;
        int len = src.length() > dst.length() ? dst.length() : src.length();
        for (int i = 0; i < len; i++) {
            if (src.charAt(i) != dst.charAt(i)) {
                return i;
            }
        }
        return len;
    }

 
}
