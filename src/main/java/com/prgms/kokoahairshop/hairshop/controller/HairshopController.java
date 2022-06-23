package com.prgms.kokoahairshop.hairshop.controller;

import com.prgms.kokoahairshop.hairshop.dto.HairshopDto;
import com.prgms.kokoahairshop.hairshop.service.HairshopService;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/hairshops")
public class HairshopController {
    private final HairshopService hairshopService;

    public HairshopController(HairshopService hairshopService) {
        this.hairshopService = hairshopService;
    }

    @PutMapping
    public ResponseEntity<Long> insert(@RequestBody HairshopDto hairshopDto) {
        HairshopDto insert = hairshopService.insert(hairshopDto);
        return ResponseEntity.created(URI.create("/api/v1/hairshop/" + insert.getId())).body(insert.getId());
    }

    @GetMapping
    public ResponseEntity<Page<HairshopDto>> getAll(Pageable pageable) {
        return ResponseEntity.ok(hairshopService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HairshopDto> getById(@PathVariable Long id) throws NotFoundException {
        HairshopDto byId = hairshopService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PatchMapping
    public ResponseEntity<Object> modify(@RequestBody HairshopDto hairshopDto) {
        hairshopService.update(hairshopDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        hairshopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
