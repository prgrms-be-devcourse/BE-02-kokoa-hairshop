package com.prgms.kokoahairshop.hairshop.service;

import com.prgms.kokoahairshop.hairshop.dto.*;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HairshopService {
    private final HairshopRepository hairshopRepository;
    private final HairshopConverter hairshopConverter;

    public HairshopService(HairshopRepository hairshopRepository, HairshopConverter hairshopConverter) {
        this.hairshopRepository = hairshopRepository;
        this.hairshopConverter = hairshopConverter;
    }

    @Transactional
    public HairshopResponse insert(CreateHairshopRequest createHairshopRequest) {
        Hairshop hairshop = hairshopConverter.convertToHairshop(createHairshopRequest);
        Hairshop entity = hairshopRepository.save(hairshop);
        return hairshopConverter.convertToHairshopResponse(entity);
    }

    @Transactional
    public Page<HairshopResponse> findAll(Pageable pageable) {
        List<HairshopResponse> list = hairshopRepository.findAll()
                .stream().map(hairshopConverter::convertToHairshopResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public HairshopResponse findById(Long id) throws NotFoundException {
        return hairshopRepository.findById(id)
                .map(hairshopConverter::convertToHairshopResponse)
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
    }

    @Transactional
    public HairshopResponse update(ModifyHairshopRequest modifyHairshopRequest) throws NotFoundException {
        // Todo : referenceById 톭아보기
        hairshopRepository.findById(modifyHairshopRequest.getId())
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
        Hairshop hairshop = hairshopConverter.convertToHairshop(modifyHairshopRequest);
        Hairshop update = hairshopRepository.save(hairshop);
        return hairshopConverter.convertToHairshopResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        hairshopRepository.deleteById(id);
        return id;
    }
}