package com.prgms.kokoahairshop.hairshop.controller;

import com.prgms.kokoahairshop.hairshop.dto.CreateHairshopRequest;
import com.prgms.kokoahairshop.hairshop.dto.HairshopResponse;
import com.prgms.kokoahairshop.hairshop.dto.ModifyHairshopRequest;
import com.prgms.kokoahairshop.hairshop.service.HairshopService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1/hairshops")
public class HairshopController {
    private final HairshopService hairshopService;

    public HairshopController(HairshopService hairshopService) {
        this.hairshopService = hairshopService;
    }

    @PostMapping
    public ResponseEntity<Long> insert(@Valid @RequestBody CreateHairshopRequest createHairshopRequest) {
        HairshopResponse insert = hairshopService.insert(createHairshopRequest);
        return ResponseEntity.created(URI.create("/api/v1/hairshops/" + insert.getId())).body(insert.getId());
    }

    @GetMapping
    public ResponseEntity<Page<HairshopResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(hairshopService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HairshopResponse> getById(@PathVariable Long id) {
        HairshopResponse byId = hairshopService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PatchMapping
    public ResponseEntity<Object> modify(@Valid @RequestBody ModifyHairshopRequest modifyHairshopRequest) {
        hairshopService.update(modifyHairshopRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        hairshopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
