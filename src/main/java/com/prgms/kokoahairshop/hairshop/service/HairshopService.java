package com.prgms.kokoahairshop.hairshop.service;

import com.prgms.kokoahairshop.hairshop.dto.HairshopConverter;
import com.prgms.kokoahairshop.hairshop.dto.HairshopDto;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public HairshopDto insert(HairshopDto hairshopDto) {
        Hairshop hairshop = hairshopConverter.createHairshopDtoToHairshop(hairshopDto);
        Hairshop entity = hairshopRepository.save(hairshop);
        return hairshopConverter.hairshopToReadHairshopDto(entity);
    }

    @Transactional
    public Page<HairshopDto> findAll(Pageable pageable) {
        List<HairshopDto> list = hairshopRepository.findAll()
                .stream().map(hairshopConverter::hairshopToReadHairshopDto)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public HairshopDto findById(Long id) throws NotFoundException {
        return hairshopRepository.findById(id)
                .map(hairshopConverter::hairshopToReadHairshopDto)
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
    }

    @Transactional
    public HairshopDto update(HairshopDto hairshopDto) {
        // Todo : referenceById 톭아보기
        Hairshop hairshop = hairshopRepository.getReferenceById(hairshopDto.getId());
        Hairshop update = hairshopRepository.save(hairshop);
        return hairshopConverter.hairshopToReadHairshopDto(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        hairshopRepository.deleteById(id);
        return id;
    }
}
