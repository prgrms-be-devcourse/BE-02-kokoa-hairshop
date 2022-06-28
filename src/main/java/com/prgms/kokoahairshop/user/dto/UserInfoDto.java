package com.prgms.kokoahairshop.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserInfoDto {

    private Long id;
    private String email;
    private String auth;

}
