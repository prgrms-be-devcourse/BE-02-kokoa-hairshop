package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignerResponse {
    private Long id;
    private String name;
    private String image;
    private String introduction;
    private String position;
    private Long hairshopId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
