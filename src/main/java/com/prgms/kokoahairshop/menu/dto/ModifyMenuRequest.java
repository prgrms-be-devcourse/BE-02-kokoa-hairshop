package com.prgms.kokoahairshop.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMenuRequest {

    @NotNull(message = "ID를 입력해주세요.")
    private Long id;

    @NotBlank(message = "공백이 입력되었습니다.")
    @Size(min = 1, max = 30, message = "이름은 1자 이상 30자 이하로 해주세요.")
    private String name;

    @NotNull(message = "가격을 입력해주세요.")
    private Integer price;

    @NotNull(message = "할인율을 입력해주세요.")
    @Min(value = 0, message = "할인율은 0~100 사이로 해주세요.")
    @Max(value = 100, message = "할인율은 0~100 사이로 해주세요.")
    private Integer discount;

    private String gender;

    private String type;

    @NotNull(message = "노출 시간을 입력해주세요.")
    private Integer exposedTime;

    @Size(max = 200, message = "이미지 URL은 200자 이하로 해주세요.")
    private String image;

    @NotNull(message = "헤어샵 ID를 입력해주세요.")
    private Long hairshopId;
}
