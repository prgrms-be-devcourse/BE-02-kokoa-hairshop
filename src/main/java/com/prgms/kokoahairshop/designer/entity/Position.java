package com.prgms.kokoahairshop.designer.entity;

public enum Position {
    원장, 실장, 디자이너;

    private static final Position[] ENUMS = Position.values();

    public static Position of(int nums) {
        if (nums < 1 || nums > 3) {
            throw new IllegalArgumentException("Invalid value for Position: " + nums);
        }
        return ENUMS[nums - 1];
    }

    public int getValue() {
        return ordinal() + 1;
    }
}
