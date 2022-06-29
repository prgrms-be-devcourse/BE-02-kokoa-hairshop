package com.prgms.kokoahairshop.designer.dto;

import com.prgms.kokoahairshop.designer.entity.Designer;
import org.springframework.stereotype.Component;

@Component
public class DesignerConverter {
    public Designer convertToDesigner(CreateDesignerRequest createDesignerRequest) {
        return Designer.builder()
                .name(createDesignerRequest.getName())
                .image(createDesignerRequest.getImage())
                .introduction(createDesignerRequest.getIntroduction())
                .position(createDesignerRequest.getPosition())
                .build();
    }

    public Designer convertToDesigner(ModifyDesignerRequest modifyDesignerRequest) {
        return Designer.builder()
                .id(modifyDesignerRequest.getId())
                .name(modifyDesignerRequest.getName())
                .image(modifyDesignerRequest.getImage())
                .introduction(modifyDesignerRequest.getIntroduction())
                .position(modifyDesignerRequest.getPosition())
                .build();
    }

    public DesignerResponse convertToDesignerResponse(Designer designer) {
        return DesignerResponse.builder()
                .id(designer.getId())
                .name(designer.getName())
                .image(designer.getImage())
                .introduction(designer.getIntroduction())
                .position(designer.getPosition())
                .createdAt(designer.getCreatedAt())
                .updatedAt(designer.getUpdatedAt())
                .build();
    }
}
