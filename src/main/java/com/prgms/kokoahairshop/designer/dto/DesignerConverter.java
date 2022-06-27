package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.entity.Position;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import org.springframework.stereotype.Component;

@Component
public class DesignerConverter {
    public Designer convertToDesigner(CreateDesignerRequest createDesignerRequest, Hairshop hairshop) {
        return Designer.builder()
                .name(createDesignerRequest.getName())
                .image(createDesignerRequest.getImage())
                .introduction(createDesignerRequest.getIntroduction())
                .position(Position.valueOf(createDesignerRequest.getPosition()).getValue())
                .hairshop(hairshop)
                .build();
    }

    public Designer convertToDesigner(ModifyDesignerRequest modifyDesignerRequest, Hairshop hairshop) {
        return Designer.builder()
                .id(modifyDesignerRequest.getId())
                .name(modifyDesignerRequest.getName())
                .image(modifyDesignerRequest.getImage())
                .introduction(modifyDesignerRequest.getIntroduction())
                .position(Position.valueOf(modifyDesignerRequest.getPosition()).getValue())
                .hairshop(hairshop)
                .build();
    }

    public DesignerResponse convertToDesignerResponse(Designer designer) {
        return DesignerResponse.builder()
                .id(designer.getId())
                .name(designer.getName())
                .image(designer.getImage())
                .introduction(designer.getIntroduction())
                .position(Position.of(designer.getPosition()).toString())
                .hairshopId(designer.getId())
                .createdAt(designer.getCreatedAt())
                .updatedAt(designer.getUpdatedAt())
                .build();
    }
}
