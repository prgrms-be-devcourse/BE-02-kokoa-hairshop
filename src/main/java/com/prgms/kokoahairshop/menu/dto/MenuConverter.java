package com.prgms.kokoahairshop.menu.dto;

import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.menu.entity.Gender;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.entity.Type;
import org.springframework.stereotype.Component;

@Component
public class MenuConverter {

    public Menu convertToMenu(CreateMenuRequest createMenuRequest, Hairshop hairshop) {
        return Menu.builder()
            .name(createMenuRequest.getName())
            .price(createMenuRequest.getPrice())
            .discount(createMenuRequest.getDiscount())
            .gender(Gender.getEnum(createMenuRequest.getGender()))
            .type(Type.getEnum(createMenuRequest.getType()))
            .exposedTime(createMenuRequest.getExposedTime())
            .image(createMenuRequest.getImage())
            .hairshop(hairshop)
            .build();
    }

    public Menu convertToMenu(ModifyMenuRequest modifyMenuRequest, Hairshop hairshop) {
        return Menu.builder()
            .id(modifyMenuRequest.getId())
            .name(modifyMenuRequest.getName())
            .price(modifyMenuRequest.getPrice())
            .discount(modifyMenuRequest.getDiscount())
            .gender(Gender.getEnum(modifyMenuRequest.getGender()))
            .type(Type.getEnum(modifyMenuRequest.getType()))
            .exposedTime(modifyMenuRequest.getExposedTime())
            .image(modifyMenuRequest.getImage())
            .hairshop(hairshop)
            .build();
    }

    public MenuResponse convertToMenuResponse(Menu menu) {
        return MenuResponse.builder()
            .id(menu.getId())
            .name(menu.getName())
            .price(menu.getPrice())
            .discount(menu.getDiscount())
            .gender(menu.getGender().getGender())
            .type(menu.getType().getType())
            .exposedTime(menu.getExposedTime())
            .image(menu.getImage())
            .hairshopId(menu.getHairshop().getId())
            .createdAt(menu.getCreatedAt())
            .updatedAt(menu.getUpdatedAt())
            .build();
    }
}
