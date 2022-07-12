package com.prgms.kokoahairshop.hairshop.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.hairshop.dto.CreateHairshopRequest;
import com.prgms.kokoahairshop.hairshop.dto.HairshopConverter;
import com.prgms.kokoahairshop.hairshop.dto.HairshopResponse;
import com.prgms.kokoahairshop.hairshop.dto.ModifyHairshopRequest;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import com.prgms.kokoahairshop.user.entity.User;
import com.prgms.kokoahairshop.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HairshopService {

    private final UserRepository userRepository;
    private final HairshopRepository hairshopRepository;
    private final HairshopConverter hairshopConverter;

    public HairshopService(UserRepository userRepository, HairshopRepository hairshopRepository,
                           HairshopConverter hairshopConverter) {
        this.userRepository = userRepository;
        this.hairshopRepository = hairshopRepository;
        this.hairshopConverter = hairshopConverter;
    }

    @Transactional(readOnly = true)
    public HairshopResponse insert(CreateHairshopRequest createHairshopRequest) {
        User user = userRepository.getReferenceById(createHairshopRequest.getUserId());
        Hairshop hairshop = hairshopConverter.convertToHairshop(createHairshopRequest, user);
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
        findHairshopById(modifyHairshopRequest.getId());
        User user = userRepository.getReferenceById(modifyHairshopRequest.getUserId());
        Hairshop hairshop = hairshopConverter.convertToHairshop(modifyHairshopRequest, user);
        Hairshop update = hairshopRepository.save(hairshop);
        return hairshopConverter.convertToHairshopResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        hairshopRepository.deleteById(id);
        return id;
    }

    @Transactional(readOnly = true)
    public Hairshop findHairshopById(Long id) {
        return hairshopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
    }
}