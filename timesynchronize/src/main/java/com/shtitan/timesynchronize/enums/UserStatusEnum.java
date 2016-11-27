package com.shtitan.timesynchronize.enums;

public enum UserStatusEnum {
	AVALIABLE(0), FORBIDDEN(1),DELETED(2);
	
	public final int value;
	
	UserStatusEnum (int value){
		this.value = value;
	}
}
