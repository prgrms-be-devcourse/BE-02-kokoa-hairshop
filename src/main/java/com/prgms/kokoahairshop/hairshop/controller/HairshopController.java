package com.prgms.kokoahairshop.hairshop.controller;

import com.prgms.kokoahairshop.hairshop.dto.CreateHairshopRequest;
import com.prgms.kokoahairshop.hairshop.dto.HairshopResponse;
import com.prgms.kokoahairshop.hairshop.dto.ModifyHairshopRequest;
import com.prgms.kokoahairshop.hairshop.entity.Hairshop;
import com.prgms.kokoahairshop.hairshop.service.HairshopService;
import com.prgms.kokoahairshop.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/hairshops")
public class HairshopController {

    private final HairshopService hairshopService;

    public HairshopController(HairshopService hairshopService) {
        this.hairshopService = hairshopService;
    }

    @PostMapping
    public ResponseEntity<Long> insert(
            @Valid @RequestBody CreateHairshopRequest createHairshopRequest, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(createHairshopRequest.getUserId())) {
            throw new IllegalArgumentException("본인의 헤어샵만 생성할 수 있습니다.");
        }
        HairshopResponse insert = hairshopService.insert(createHairshopRequest);
        return ResponseEntity.created(URI.create("/hairshops/" + insert.getId()))
                .body(insert.getId());
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
    public ResponseEntity<Object> modify(
            @Valid @RequestBody ModifyHairshopRequest modifyHairshopRequest, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(modifyHairshopRequest.getUserId())) {
            throw new IllegalArgumentException("본인의 헤어샵만 수정할 수 있습니다.");
        }
        hairshopService.update(modifyHairshopRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id, @AuthenticationPrincipal User user) {
        Hairshop hairshop = hairshopService.findHairshopById(id);
        log.info("hairshop: {}", hairshop);
        if (!user.getId().equals(hairshop.getUser().getId())) {
            throw new IllegalArgumentException("본인의 헤어샵만 삭제할 수 있습니다.");
        }
        hairshopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
