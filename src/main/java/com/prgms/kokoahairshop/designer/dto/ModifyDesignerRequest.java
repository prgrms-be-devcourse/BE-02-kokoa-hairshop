package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyDesignerRequest {
    private Long id;
    private String name;
    private String image;
    private String introduction;
    private Position position;
    private Long hairshopId;
}
