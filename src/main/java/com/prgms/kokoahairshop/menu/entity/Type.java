package com.prgms.kokoahairshop.menu.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Type {
    커트("haircut"),
    펌("perm"),
    컬러("color"),
    클리닉("clinic"),
    스타일링("styling"),
    붙임머리("extensions"),
    메이크업("makeup");

    private static final Map<String, Type> ENUM_MAP =
            Stream.of(values()).collect(Collectors.toMap(Type::getType, o -> o));

    private final String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static Type getEnum(String type) {
        return ENUM_MAP.get(type);
    }
}
