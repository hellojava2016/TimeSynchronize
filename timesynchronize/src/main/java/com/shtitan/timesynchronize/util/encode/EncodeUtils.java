/**
 * Copyright (c) 2005-2010 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: EncodeUtils.java 1211 2010-09-10 16:20:45Z calvinxiu $
 */
package com.shtitan.timesynchronize.util.encode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * 各种格式的编码加码工具类.
 * 
 * 集成Commons-Codec,Commons-Lang及JDK提供的编解码方法.
 * 
 * @author calvin
 */
public class EncodeUtils {

	private static final String DEFAULT_URL_ENCODING = "UTF-8";

	/**
	 * Hex编码.
	 */
	public static String hexEncode(byte[] input) {
		return Hex.encodeHexString(input);
	}

	/**
	 * Hex解码.
	 */
	public static byte[] hexDecode(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码.
	 */
	public static String base64Encode(byte[] input) {
		return new String(Base64.encodeBase64(input));
	}

	/**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
	public static String base64UrlSafeEncode(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	/**
	 * Base64解码.
	 */
	public static byte[] base64Decode(String input) {
		return Base64.decodeBase64(input);
	}

	/**
	 * URL 编码, Encode默认为UTF-8. 
	 */
	public static String urlEncode(String input) {
		try {
			return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * URL 解码, Encode默认为UTF-8. 
	 */
	public static String urlDecode(String input) {
		try {
			return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("Unsupported Encoding Exception", e);
		}
	}

	/**
	 * Html 转码.
	 */
	public static String htmlEscape(String html) {
		return StringEscapeUtils.escapeHtml4(html);
	}

	/**
	 * Html 解码.
	 */
	public static String htmlUnescape(String htmlEscaped) {
		return StringEscapeUtils.unescapeHtml4(htmlEscaped);
	}

	/**
	 * Xml 转码.
	 */
	public static String xmlEscape(String xml) {
		return StringEscapeUtils.escapeXml(xml);
	}

	/**
	 * Xml 解码.
	 */
	public static String xmlUnescape(String xmlEscaped) {
		return StringEscapeUtils.unescapeXml(xmlEscaped);
	}
	
	/**
	 * MD5编码
	 * @param originstr
	 * @return
	 */
	public static String ecodeByMD5(String originstr){
        String result = null;
        char hexDigits[] = {//用来将字节转换成 16 进制表示的字符
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        if(originstr != null){
            try {
                //返回实现指定摘要算法的 MessageDigest 对象
                MessageDigest md = MessageDigest.getInstance("MD5"); 
                //使用utf-8编码将originstr字符串编码并保存到source字节数组
                byte[] source = originstr.getBytes("utf-8");
                //使用指定的 byte 数组更新摘要
                md.update(source);
                //通过执行诸如填充之类的最终操作完成哈希计算，结果是一个128位的长整数
                byte[] tmp = md.digest();
                //用16进制数表示需要32位
                char[] str = new char[32];
                for(int i=0,j=0; i < 16; i++){
                    //j表示转换结果中对应的字符位置
                    //从第一个字节开始，对 MD5 的每一个字节
                    //转换成 16 进制字符
                    byte b = tmp[i];
                    //取字节中高 4 位的数字转换
                    //无符号右移运算符>>> ，它总是在左边补0
                    //0x代表它后面的是十六进制的数字. f转换成十进制就是15
                    str[j++] = hexDigits[b>>>4 & 0xf];
                    // 取字节中低 4 位的数字转换
                    str[j++] = hexDigits[b&0xf];
                }
                result = new String(str);//结果转换成字符串用于返回
            } catch (NoSuchAlgorithmException e) {
                //当请求特定的加密算法而它在该环境中不可用时抛出此异常
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                //不支持字符编码异常
                e.printStackTrace();
            }
        }
        return result;

    }
	
	public static void main(String[] args) {
		System.out.println(ecodeByMD5("BJBK:01071193387580474171410454334001:1410853464168"));
		System.out.println(ecodeByMD5("admin"));
		System.out.println(ecodeByMD5("bjbk"));
		System.out.println(ecodeByMD5("bhbk"));
		System.out.println(ecodeByMD5("bjbkadmin"));
		System.out.println(ecodeByMD5("bjrisk"));
		System.out.println(ecodeByMD5("bjasset"));
		System.out.println(ecodeByMD5("bjreport"));
	}
}
