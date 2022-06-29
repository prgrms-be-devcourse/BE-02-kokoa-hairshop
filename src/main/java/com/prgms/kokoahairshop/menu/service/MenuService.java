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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {
    private final HairshopRepository hairshopRepository;
    private final MenuRepository menuRepository;
    private final MenuConverter menuConverter;

    @Transactional(readOnly = true)
    public MenuResponse insert(CreateMenuRequest createMenuRequest) throws NotFoundException {
        Hairshop hairshop = hairshopRepository.findById(createMenuRequest.getHairshopId())
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
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
    public Page<MenuResponse> findByHairshopId(Pageable pageable, Long id) throws NotFoundException {
        Hairshop hairshop = hairshopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
        List<MenuResponse> list = menuRepository.findByHairshop(hairshop)
                .stream().map(menuConverter::convertToMenuResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public MenuResponse findById(Long id) throws NotFoundException {
        return menuRepository.findById(id)
                .map(menuConverter::convertToMenuResponse)
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다."));
    }

    @Transactional
    public MenuResponse update(ModifyMenuRequest modifyMenuRequest) throws NotFoundException {
        Hairshop hairshop = hairshopRepository.findById(modifyMenuRequest.getHairshopId())
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
        menuRepository.findById(modifyMenuRequest.getId())
                .orElseThrow(() -> new NotFoundException("메뉴를 찾을 수 없습니다."));
        Menu menu = menuConverter.convertToMenu(modifyMenuRequest, hairshop);
        Menu update = menuRepository.save(menu);
        return menuConverter.convertToMenuResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        menuRepository.deleteById(id);
        return id;
    }
}
