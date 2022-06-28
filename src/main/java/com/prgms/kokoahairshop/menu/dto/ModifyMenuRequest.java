package com.prgms.kokoahairshop.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyMenuRequest {
    private Long id;
    private String name;
    private Integer price;
    private Integer discount;
    private String gender;
    private String type;
    private Integer exposedTime;
    private String image;
    private Long hairshopId;
}
