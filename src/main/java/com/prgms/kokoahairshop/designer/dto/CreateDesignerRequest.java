package com.prgms.kokoahairshop.designer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDesignerRequest {
    private String name;
    private String image;
    private String introduction;
    private String position;
    private String hairshopId;
}
