package com.nearloo.nearloo_backend.controller;

import com.nearloo.nearloo_backend.repository.UserRepository;
import com.nearloo.nearloo_backend.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/toilets")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final UserRepository userRepo;

    @PostMapping("/{id}/report")
    public ResponseEntity<?> report(
            @PathVariable UUID id,
            @RequestParam String reportType, // clean or dirty
            Authentication auth) {
        UUID userId = userRepo.findByEmail(auth.getName())
                .orElseThrow().getId();
        String result = reportService.submitReport(id, userId, reportType);
        return ResponseEntity.ok(result);
    }
}
