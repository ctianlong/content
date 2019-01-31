package com.netease.homework.content.config.constant;

import com.netease.snailreader.common.component.enumeration.IntEnum;

/**
 * @Description
 * @Auther ctl
 * @Date 2019/1/27
 */
public enum ImgType implements IntEnum {

    EXTERNAL_URL(1, "外部URL"),
    LOCAL_UPLOAD(2, "本地上传");

    private final int intValue;
    private final String displayName;

    ImgType(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    @Override
    public int getIntValue() {
        return this.intValue;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public boolean is(int intValue) {
        return this.intValue == intValue;
    }

    public boolean is(String displayName) {
        return this.displayName.equalsIgnoreCase(displayName);
    }

    public static ImgType parse(int intValue) {
        for (ImgType imgType : values())
            if (imgType.is(intValue))
                return imgType;
        return null;
    }

    public static ImgType parse(String displayName) {
        for (ImgType imgType: values()) {
            if (imgType.is(displayName)) {
                return imgType;
            }
        }
        return null;
    }

}
