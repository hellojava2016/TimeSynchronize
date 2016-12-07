/*
 * $Id: YotcException.java, 2009-2-9 上午11:10:33 Victor Exp $
 * 
 * Copyright (c) 2008 Wuhan Yangtze Optical Technology Co., Ltd 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by YOTC or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.shtitan.timesynchronize.exception;

/**
 * <p>
 * Title: YotcException
 * </p>
 * <p>
 * 系统根异常
 * </p>
 * 
 * @author Victor
 * @created 2009-2-9 上午11:10:33
 * @modified aaron lee 调整异常结构 2009-8-8
 * @check [who date description]
 */
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 5893725839548086058L;
    private int errorCode;
    private String[] source;

    /**
     * 不能为空
     */
    public static final int INPUT_IS_NULL=1; //{0}不能为空
    public static final int INPUT_IS_EXIST=2;//{0}输入已经存在
    
    public static final int SYNC_COMMON_ERROR = 2000;
    
    public BaseException(int errorCode, String... source){
        super();
        this.errorCode = errorCode;
        this.source = source;
    }
    
    public BaseException(int errorCode,Throwable th,String... source) {
        super(th);
        this.errorCode = errorCode;
        this.source = source;
    }
    
    public BaseException(Throwable th){
        super(th);
    }

    
    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the source
     */
    public String[] getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String[] source) {
        this.source = source;
    }
}
