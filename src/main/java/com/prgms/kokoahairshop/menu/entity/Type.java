package com.prgms.kokoahairshop.menu.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Type {
    haircut("커트"),
    perm("펌"),
    color("컬러"),
    clinic("클리닉"),
    styling("스타일링"),
    extensions("붙임머리"),
    makeup("메이크업");

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
