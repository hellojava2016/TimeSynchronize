package com.shtitan.timesynchronize.enums;

public enum EqObjectType {
	ne(1,Boolean.TRUE),
    psne(2,Boolean.TRUE),
    isne(3,Boolean.TRUE),
    rack(4,Boolean.FALSE),
    shelf(5,Boolean.FALSE),
    slot(6,Boolean.FALSE),
    port(7,Boolean.FALSE),
    pvc(8,Boolean.FALSE);

    private int value;
    /* 是否为网元 */
    private Boolean isNe;

    /**
     * @param value
     * @param isNe
     *            网元管理对象是否为网元
     */
    private EqObjectType(int value, Boolean isNe) {
        this.value = value;
        this.isNe = isNe;
    }

    /**
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 判断管理对象是否为网管
     * 
     * @return
     */
    public Boolean isNe() {
        return isNe;
    }

    /**
     * 根据设备管理对象名称获取其对应的枚举类型
     * 
     * @param objecType
     * @return
     */
    public static EqObjectType getEqObjectType(String objectType) {
        for (EqObjectType eqObjectType : EqObjectType.values()) {
            if (eqObjectType.toString().equals(objectType)) {
                return eqObjectType;
            }
        }
        return null;
//        return EqObjectType.slot;
    }
}
