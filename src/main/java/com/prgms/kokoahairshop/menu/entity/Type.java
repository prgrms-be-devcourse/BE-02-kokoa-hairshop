package com.prgms.kokoahairshop.menu.entity;

public enum Type {
    커트, 펌, 컬러, 클리닉, 스타일링, 붙임머리, 메이크업;

    public static final Type[] ENUMS = Type.values();

    public static Type of(int nums) {
        if (nums < 1 || nums > 7) {
            throw new IllegalArgumentException("Invalid value for Position: " + nums);
        }
        return ENUMS[nums - 1];
    }

    public int getValue() {
        return ordinal() + 1;
    }
}
