package com.prgms.kokoahairshop.menu.controller;

import com.prgms.kokoahairshop.menu.dto.CreateMenuRequest;
import com.prgms.kokoahairshop.menu.dto.MenuResponse;
import com.prgms.kokoahairshop.menu.dto.ModifyMenuRequest;
import com.prgms.kokoahairshop.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<Long> insert(@Valid @RequestBody CreateMenuRequest createMenuRequest) {
        MenuResponse insert = menuService.insert(createMenuRequest);
        return ResponseEntity.created(URI.create("/menu/" + insert.getId())).body(insert.getId());
    }

    @GetMapping
    public ResponseEntity<Page<MenuResponse>> getAll(Pageable pageable) {
        return ResponseEntity.ok(menuService.findAll(pageable));
    }

    @GetMapping("/hairshops/{id}")
    public ResponseEntity<Page<MenuResponse>> getByHairshop(Pageable pageable, @PathVariable Long id) {
        return ResponseEntity.ok(menuService.findByHairshopId(pageable, id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponse> getById(@PathVariable Long id) {
        MenuResponse byId = menuService.findById(id);
        return ResponseEntity.ok(byId);
    }

    @PatchMapping
    public ResponseEntity<Object> modify(@Valid @RequestBody ModifyMenuRequest modifyMenuRequest) {
        menuService.update(modifyMenuRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        menuService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

