package com.prgms.kokoahairshop.user.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
    private  LocalDateTime timestamp = LocalDateTime.now();
    private  int status;
    private  String message;

}
