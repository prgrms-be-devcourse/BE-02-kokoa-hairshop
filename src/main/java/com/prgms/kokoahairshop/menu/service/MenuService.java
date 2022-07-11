package com.prgms.kokoahairshop.menu.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.menu.dto.CreateMenuRequest;
import com.prgms.kokoahairshop.menu.dto.MenuConverter;
import com.prgms.kokoahairshop.menu.dto.MenuResponse;
import com.prgms.kokoahairshop.menu.dto.ModifyMenuRequest;
import com.prgms.kokoahairshop.menu.entity.Menu;
import com.prgms.kokoahairshop.menu.repository.MenuRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final HairshopRepository hairshopRepository;
    private final MenuRepository menuRepository;
    private final MenuConverter menuConverter;
    private static final String HAIRSHOP_NOT_FOUND = "헤어샵을 찾을 수 없습니다.";
    private static final String MENU_NOT_FOUND = "메뉴을 찾을 수 없습니다.";

    @Transactional(readOnly = true)
    public MenuResponse insert(CreateMenuRequest createMenuRequest) throws NotFoundException {
        Hairshop hairshop = findHairshopById(createMenuRequest.getHairshopId());
        Menu menu = menuConverter.convertToMenu(createMenuRequest, hairshop);
        Menu entity = menuRepository.save(menu);
        return menuConverter.convertToMenuResponse(entity);
    }

    @Transactional
    public Page<MenuResponse> findAll(Pageable pageable) {
        List<MenuResponse> list = menuRepository.findAll()
            .stream().map(menuConverter::convertToMenuResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public Page<MenuResponse> findByHairshopId(Pageable pageable, Long id)
        throws NotFoundException {
        Hairshop hairshop = findHairshopById(id);
        List<MenuResponse> list = menuRepository.findByHairshop(hairshop)
            .stream().map(menuConverter::convertToMenuResponse)
            .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public MenuResponse findById(Long id) throws NotFoundException {
        return menuRepository.findById(id)
            .map(menuConverter::convertToMenuResponse)
            .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
    }

    @Transactional
    public MenuResponse update(ModifyMenuRequest modifyMenuRequest) throws NotFoundException {
        Hairshop hairshop = findHairshopById(modifyMenuRequest.getHairshopId());
        findMenuById(modifyMenuRequest.getId());
        Menu menu = menuConverter.convertToMenu(modifyMenuRequest, hairshop);
        Menu update = menuRepository.save(menu);
        return menuConverter.convertToMenuResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        menuRepository.deleteById(id);
        return id;
    }

    @Transactional(readOnly = true)
    public Hairshop findHairshopById(Long id){
        return hairshopRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(HAIRSHOP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Menu findMenuById(Long id){
        return menuRepository.findById(id)
            .orElseThrow(() -> new NotFoundException(MENU_NOT_FOUND));
    }
}
