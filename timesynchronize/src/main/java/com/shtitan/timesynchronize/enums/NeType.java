package com.shtitan.timesynchronize.enums;

import com.shtitan.timesynchronize.entity.eqm.bo.NetworkElement;

public enum NeType {
	  unknown(NetworkElement.class,"UNKNOWN",false);   //未知网元类型
	
	
	    boolean subordinate;
	    private Class<? extends NetworkElement> neClass;
	    private String name;
	    
	    private NeType(Class<? extends NetworkElement> neClass,String name,boolean subordinate) {
	        this.neClass = neClass;
	        this.name=name;
	        this.subordinate=subordinate;
	    }
	    
	    public boolean isSubordinate() {
	        return subordinate;
	    }

	    public NetworkElement newInstance() {
	        try {
	            return (NetworkElement) neClass.newInstance();
	        } catch (InstantiationException e) {
	            e.printStackTrace();
	        } catch (IllegalAccessException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }
	    
	    public String toString(){
	        if(neClass==null)
	            return name();
	        else
	            return name;
	    }
}
