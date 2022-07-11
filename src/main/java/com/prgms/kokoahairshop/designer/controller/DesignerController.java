package com.prgms.kokoahairshop.designer.controller;

import com.prgms.kokoahairshop.designer.dto.CreateDesignerRequest;
import com.prgms.kokoahairshop.designer.dto.DesignerResponse;
import com.prgms.kokoahairshop.designer.dto.ModifyDesignerRequest;
import com.prgms.kokoahairshop.designer.entity.Designer;
import com.prgms.kokoahairshop.designer.service.DesignerService;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.user.entity.User;
import java.net.URI;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/designers")
public class DesignerController {

    private final DesignerService designerService;

    public DesignerController(DesignerService designerService) {
        this.designerService = designerService;
    }

    @PostMapping
    public ResponseEntity<Long> insert(
        @Valid @RequestBody CreateDesignerRequest createDesignerRequest, @AuthenticationPrincipal User user) {
        Hairshop hairshop = designerService.findHairshopById(createDesignerRequest.getHairshopId());
        if (!hairshop.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("본인의 헤어샵 디자이너만 생성할 수 있습니다.");
        }
        DesignerResponse insert = designerService.insert(createDesignerRequest);
        return ResponseEntity.created(URI.create("/designers/" + insert.getId()))
            .body(insert.getId());
    }

    @GetMapping
    public ResponseEntity<Page<DesignerResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(designerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignerResponse> getById(@PathVariable Long id) {
        DesignerResponse byId = designerService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @GetMapping("/hairshop/{id}")
    public ResponseEntity<Page<DesignerResponse>> getByHairshopId(Pageable pageable,
        @PathVariable Long id) {
        return ResponseEntity.ok(designerService.findByHairshopId(pageable, id));
    }

    @PatchMapping
    public ResponseEntity<Object> modify(
        @Valid @RequestBody ModifyDesignerRequest modifyDesignerRequest, @AuthenticationPrincipal User user) {
        Hairshop hairshop = designerService.findHairshopById(modifyDesignerRequest.getHairshopId());
        if (!hairshop.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("본인의 헤어샵 디자이너만 수정할 수 있습니다.");
        }
        designerService.update(modifyDesignerRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Designer designer = designerService.findDesignerById(id);
        if (!designer.getHairshop().getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("본인의 헤어샵 디자이너만 삭제할 수 있습니다.");
        }
        designerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
