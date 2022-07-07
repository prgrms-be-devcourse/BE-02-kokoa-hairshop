package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Position;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DesignerResponse {

    private Long id;

    private String name;

    private String image;

    private String introduction;

    private Position position;

    private Long hairshopId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
