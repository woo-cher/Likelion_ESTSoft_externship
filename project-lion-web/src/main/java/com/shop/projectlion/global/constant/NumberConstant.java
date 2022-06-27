package com.shop.projectlion.global.constant;

public enum NumberConstant {

    PAGE_DEFAULT_COUNT(0),
    PAGE_BTN_MAX_COUNT(5),
    PAGE_ITEM_MAX_COUNT(6),

    ITEM_IMAGE_MAX_COUNT(5);

    private final int value;

    NumberConstant(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
