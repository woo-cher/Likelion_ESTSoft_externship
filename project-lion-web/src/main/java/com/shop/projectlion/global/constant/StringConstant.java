package com.shop.projectlion.global.constant;

public enum StringConstant {

    IMAGE_REQUEST_PREFIX("/images/");

    private final String value;

    StringConstant(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
