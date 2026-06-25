package com.nearloo.nearloo_backend.service;

import com.nearloo.nearloo_backend.dto.ToiletDTO;
import com.nearloo.nearloo_backend.entity.*;
import com.nearloo.nearloo_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ToiletService {

    private final ToiletRepository toiletRepo;
    private final UserRepository userRepo;

    public List<Toilet> getAllToilets() {
        return toiletRepo.findAll();
    }

    // Find nearby toilets using Haversine formula
    public List<Toilet> findNearby(double lat, double lng, double radiusKm) {
        return toiletRepo.findNearby(lat, lng, radiusKm);
    }

    public Toilet getById(UUID id) {
        return toiletRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Toilet not found"));
    }

    // Admin: add new toilet
    public Toilet create(ToiletDTO dto, String adminEmail) {
        User admin = userRepo.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Toilet toilet = Toilet.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .isFemaleOnly(dto.getIsFemaleOnly())
                .isAccessible(dto.getIsAccessible())
                .isPaid(dto.getIsPaid())
                .is24hr(dto.getIs24hr())
                .operatingHours(dto.getOperatingHours())
                .status("clean")
                .addedBy(admin)
                .build();
        return toiletRepo.save(toilet);
    }

    // Admin: update toilet info
    public Toilet update(UUID id, ToiletDTO dto) {
        Toilet toilet = getById(id);
        toilet.setName(dto.getName());
        toilet.setAddress(dto.getAddress());
        toilet.setStatus(dto.getStatus());
        toilet.setUpdatedAt(LocalDateTime.now());
        return toiletRepo.save(toilet);
    }

    // Admin: update status only
    public Toilet updateStatus(UUID id, String newStatus) {
        Toilet toilet = getById(id);
        toilet.setStatus(newStatus);
        toilet.setUpdatedAt(LocalDateTime.now());
        return toiletRepo.save(toilet);
    }

    public void delete(UUID id) {
        toiletRepo.deleteById(id);
    }
}
