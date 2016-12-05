package com.shtitan.timesynchronize.enums;

import java.util.ArrayList;
import java.util.List;

public enum DeviceType implements EnumValue{
	STANDARD(0x111C, null, NeType.unknown,0, false),//code假的
    UNKNOWN(0x2fff, null, NeType.unknown,0, true);

    
    DeviceType parent;// 仅仅用于OID映射的继承处理
    
    // 设备类型唯一标识码
    Integer code;
    // 设备类型对应的网元类型
    NeType neType;

    int ethernetPortCount;
    // 该设备类型的网元是否被托管
    boolean isProxied;

   
    
    // 这个name为oem考虑,在客户端展示只能使用toString,不能使用name()
    String displayName;
    
    private DeviceType(String name){
        valueOf(name);
    }
    
    private DeviceType(Integer code, DeviceType parent, NeType neType,int ethernetPortCount,
            boolean isProxied) {
    	 this.parent = parent;
         this.code = code;
         this.neType = neType;
         this.isProxied = isProxied;
         this.ethernetPortCount=ethernetPortCount;
    }

   
    
	@Override
	public int getValue() {
		return code;
	}
	
	 @Override
	    public String toString() {
	        if(displayName!=null)
	            return displayName;
	        return name();
	    }
	    
	    /** 大类+型号 */
	    public String toStringWithNeType(){
	        if(this.equals(DeviceType.UNKNOWN))
	            return toString();
	        return neType.toString()+":"+toString();
	    }
	    
	    /**
	     * 通过显示name得到实例
	     * @param name 显示的name,不是name()返回的name
	     * @return
	     */
	    public static DeviceType getDeviceType(String displayName){
	        for(DeviceType type : DeviceType.values()){
	            if(type.toString().equals(displayName)){
	                return type;
	            }
	        }
	        return DeviceType.UNKNOWN;
	    }
	    
	    /**
	     * 通过显示name得到实例
	     * @param name 显示的name,不是name()返回的name
	     * @return
	     */
	    public static List<DeviceType> getDeviceTypes(String displayName){
	    	List<DeviceType> list = new ArrayList<DeviceType>();
	        for(DeviceType type : DeviceType.values()){
	            if(type.toString().equals(displayName)){
	                list.add(type);
	            }
	        }
	        return list;
	    }
	    
	    /**
	     * 根据Code得到DeviceType
	     * @param code
	     * @return
	     */
	    public static DeviceType getDeviceTypeByCode(int code){
	        for(DeviceType type : DeviceType.values()){
	            if(type.code.equals(code)){
	                return type;
	            }
	        }
	        return DeviceType.UNKNOWN;
	    }

		public DeviceType getParent() {
			return parent;
		}

		public void setParent(DeviceType parent) {
			this.parent = parent;
		}

		public Integer getCode() {
			return code;
		}

		public void setCode(Integer code) {
			this.code = code;
		}

		public NeType getNeType() {
			return neType;
		}

		public void setNeType(NeType neType) {
			this.neType = neType;
		}

		public int getEthernetPortCount() {
			return ethernetPortCount;
		}

		public void setEthernetPortCount(int ethernetPortCount) {
			this.ethernetPortCount = ethernetPortCount;
		}

		public boolean isProxied() {
			return isProxied;
		}

		public void setProxied(boolean isProxied) {
			this.isProxied = isProxied;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
    
	    
	    
}
