package com.netease.homework.content.config.constant;

import com.netease.snailreader.common.component.enumeration.IntEnum;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/15
 */
public enum Role implements IntEnum {

    BUYER(1, "buyer"),
    SELLER(2, "seller");

    private final int intValue;
    private final String displayName;

    Role(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    @Override
    public int getIntValue() {
        return intValue;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean is(int intValue) {
        return this.intValue == intValue;
    }

    public boolean is(String displayName) {
        return this.displayName.equalsIgnoreCase(displayName);
    }

    public static Role parse(int intValue) {
        for (Role role : values())
            if (role.is(intValue))
                return role;
        return null;
    }

    public static Role parse(String displayName) {
        for (Role role: values()) {
            if (role.is(displayName)) {
                return role;
            }
        }
        return null;
    }

}
