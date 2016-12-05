
package com.shtitan.timesynchronize.enums;

public enum ConnectionState implements EnumValue {
    // 正常连接
    LINKUP(1),
    // 断连
    LINKDOWN(0),
    // 掉电
    POWEROFF(2);

    private int value;

    private ConnectionState(int value) {
        this.value = value;
    }

    @Override
    public int getValue() {
        return value;
    }
}
