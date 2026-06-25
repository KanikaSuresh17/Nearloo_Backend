package com.nearloo.nearloo_backend.controller;

import com.nearloo.nearloo_backend.dto.ToiletDTO;
import com.nearloo.nearloo_backend.service.ToiletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ToiletController {

    private final ToiletService toiletService;

    // Public — no auth needed
    @GetMapping("/toilets/nearby")
    public ResponseEntity<?> getNearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "1.0") double radius) {
        return ResponseEntity.ok(toiletService.findNearby(lat, lng, radius));
    }

    @GetMapping("/toilets/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(toiletService.getById(id));
    }

    @GetMapping("/toilets")
    public ResponseEntity<?> getAllToilets() {
        return ResponseEntity.ok(toiletService.getAllToilets());
    }

    // Admin only
    @PostMapping("/admin/toilets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody ToiletDTO dto,
            Authentication auth) {
        return ResponseEntity.ok(toiletService.create(dto, auth.getName()));
    }

    @PutMapping("/admin/toilets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable UUID id,
            @RequestBody ToiletDTO dto) {
        return ResponseEntity.ok(toiletService.update(id, dto));
    }

    @PutMapping("/admin/toilets/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateStatus(@PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(toiletService.updateStatus(id, status));
    }

    @DeleteMapping("/admin/toilets/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        toiletService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}