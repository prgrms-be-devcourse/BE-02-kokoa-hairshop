package com.prgms.kokoahairshop.designer.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Position {
    DIRECTOR("원장"),
    MANAGER("실장"),
    DESIGNER("디자이너");

    private static final Map<String, Position> ENUM_MAP =
            Stream.of(values()).collect(Collectors.toMap(Position::getPosition, o -> o));

    private final String position;

    Position(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public static Position getEnum(String position) {
        return ENUM_MAP.get(position);
    }
}
