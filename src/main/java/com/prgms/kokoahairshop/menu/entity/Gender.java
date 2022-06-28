package com.prgms.kokoahairshop.menu.entity;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Gender {
    남("man"),
    녀("woman"),
    공용("unisex");

    private static final Map<String, Gender> ENUM_MAP =
            Stream.of(values()).collect(Collectors.toMap(Gender::getGender, o -> o));

    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public static Gender getEnum(String gender) {
        return ENUM_MAP.get(gender);
    }
}
