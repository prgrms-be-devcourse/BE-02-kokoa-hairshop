package com.prgms.kokoahairshop.designer.controller;

import com.prgms.kokoahairshop.designer.dto.CreateDesignerRequest;
import com.prgms.kokoahairshop.designer.dto.DesignerResponse;
import com.prgms.kokoahairshop.designer.dto.ModifyDesignerRequest;
import com.prgms.kokoahairshop.designer.service.DesignerService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/designers")
public class DesignerController {
    private final DesignerService designerService;

    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<Object> notFoundHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> internalServerErrorHandler(Exception e) {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }

    @PostMapping
    public ResponseEntity<Long> insert(@RequestBody CreateDesignerRequest createDesignerRequest) throws NotFoundException {
        DesignerResponse insert = designerService.insert(createDesignerRequest);
        return ResponseEntity.created(URI.create("/designers/" + insert.getId())).body(insert.getId());
    }

    @GetMapping
    public ResponseEntity<Page<DesignerResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(designerService.findAll(pageable));
    }

    @GetMapping("/hairshop/{id}")
    public ResponseEntity<Page<DesignerResponse>> getByHairshop(Pageable pageable, @PathVariable Long id) throws NotFoundException {
        return ResponseEntity.ok(designerService.findByHairshopId(pageable, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DesignerResponse> getById(@PathVariable Long id) throws NotFoundException {
        DesignerResponse byId = designerService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PatchMapping
    public ResponseEntity<Object> modify(@RequestBody ModifyDesignerRequest modifyDesignerRequest) throws NotFoundException {
        designerService.update(modifyDesignerRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        designerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
