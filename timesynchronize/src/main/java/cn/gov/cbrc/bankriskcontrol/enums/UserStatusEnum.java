package cn.gov.cbrc.bankriskcontrol.enums;

public enum UserStatusEnum {
	AVALIABLE(0), FORBIDDEN(1),DELETED(2);
	
	public final int value;
	
	UserStatusEnum (int value){
		this.value = value;
	}
}
