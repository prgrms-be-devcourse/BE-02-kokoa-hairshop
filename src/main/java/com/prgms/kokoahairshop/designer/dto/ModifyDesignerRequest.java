package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyDesignerRequest {
    @NotNull(message = "디자이너 ID가 필요합니다.")
    private Long id;
    @NotBlank(message = "공백이 입력되었습니다.")
    @Size(min = 1, max = 20, message = "이름은 1자 이상 20자 이하로 해주세요.")
    private String name;
    @NotNull(message = "사진을 등록해주세요.")
    @Size(max = 200, message = "이미지 URL은 200자 이하로 해주세요.")
    private String image;
    @Size(min = 1, max = 200, message = "소개글은 200자 이하로 해주세요.")
    private String introduction;
    private Position position;
    @NotNull(message = "헤어샵 ID가 필요합니다.")
    private Long hairshopId;
}
