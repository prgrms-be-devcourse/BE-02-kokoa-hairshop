package com.prgms.kokoahairshop.designer.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.designer.dto.CreateDesignerRequest;
import com.prgms.kokoahairshop.designer.dto.DesignerConverter;
import com.prgms.kokoahairshop.designer.dto.DesignerResponse;
import com.prgms.kokoahairshop.designer.dto.ModifyDesignerRequest;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.repository.HairshopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DesignerService {

    private final HairshopRepository hairshopRepository;
    private final DesignerRepository designerRepository;
    private final DesignerConverter designerConverter;
    private static final String HAIRSHOP_NOT_FOUND = "헤어샵을 찾을 수 없습니다.";
    private static final String DESIGNER_NOT_FOUND = "디자이너를 찾을 수 없습니다.";

    @Transactional(readOnly = true)
    public DesignerResponse insert(CreateDesignerRequest createDesignerRequest) {
        Hairshop hairshop = findHairshopById(createDesignerRequest.getHairshopId());
        Designer designer = designerConverter.convertToDesigner(createDesignerRequest, hairshop);
        Designer entity = designerRepository.save(designer);
        return designerConverter.convertToDesignerResponse(entity);
    }

    @Transactional
    public Page<DesignerResponse> findAll(Pageable pageable) {
        List<DesignerResponse> list = designerRepository.findAll()
                .stream().map(designerConverter::convertToDesignerResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public DesignerResponse findById(Long id) throws NotFoundException {
        return designerRepository.findById(id)
                .map(designerConverter::convertToDesignerResponse)
                .orElseThrow(() -> new NotFoundException(HAIRSHOP_NOT_FOUND));
    }

    @Transactional
    public Page<DesignerResponse> findByHairshopId(Pageable pageable, Long id)
            throws NotFoundException {
        List<DesignerResponse> list = designerRepository.findByHairshopId(id)
                .stream().map(designerConverter::convertToDesignerResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional
    public DesignerResponse update(ModifyDesignerRequest modifyDesignerRequest)
            throws NotFoundException {
        Hairshop hairshop = findHairshopById(modifyDesignerRequest.getId());
        findDesignerById(modifyDesignerRequest.getId());
        Designer designer = designerConverter.convertToDesigner(modifyDesignerRequest, hairshop);
        Designer update = designerRepository.save(designer);
        return designerConverter.convertToDesignerResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        designerRepository.deleteById(id);
        return id;
    }

    @Transactional(readOnly = true)
    public Hairshop findHairshopById(Long id) {
        return hairshopRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(HAIRSHOP_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public Designer findDesignerById(Long id) {
        return designerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DESIGNER_NOT_FOUND));
    }
}