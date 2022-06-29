package com.prgms.kokoahairshop.designer.service;

import com.prgms.kokoahairshop.common.exception.NotFoundException;
import com.prgms.kokoahairshop.designer.dto.CreateDesignerRequest;
import com.prgms.kokoahairshop.designer.dto.DesignerConverter;
import com.prgms.kokoahairshop.designer.dto.DesignerResponse;
import com.prgms.kokoahairshop.designer.dto.ModifyDesignerRequest;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.repository.DesignerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignerService {
    private final DesignerRepository designerRepository;
    private final DesignerConverter designerConverter;

    public DesignerService(DesignerRepository designerRepository, DesignerConverter designerConverter) {
        this.designerRepository = designerRepository;
        this.designerConverter = designerConverter;
    }

    @Transactional(readOnly = true)
    public DesignerResponse insert(CreateDesignerRequest createDesignerRequest) {
        Designer designer = designerConverter.convertToDesigner(createDesignerRequest);
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
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
    }

    @Transactional
    public DesignerResponse update(ModifyDesignerRequest modifyDesignerRequest) throws NotFoundException {
        designerRepository.findById(modifyDesignerRequest.getId())
                .orElseThrow(() -> new NotFoundException("헤어샵을 찾을 수 없습니다."));
        Designer designer = designerConverter.convertToDesigner(modifyDesignerRequest);
        Designer update = designerRepository.save(designer);
        return designerConverter.convertToDesignerResponse(update);
    }

    @Transactional
    public Long deleteById(Long id) {
        designerRepository.deleteById(id);
        return id;
    }
}