package com.nearloo.nearloo_backend.controller;

import com.nearloo.nearloo_backend.entity.Rating;
import com.nearloo.nearloo_backend.entity.Toilet;
import com.nearloo.nearloo_backend.entity.User;
import com.nearloo.nearloo_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/toilets")
@RequiredArgsConstructor
public class RatingController {

    private final RatingRepository ratingRepo;
    private final ToiletRepository toiletRepo;
    private final UserRepository userRepo;

    @PostMapping("/{id}/rating")
    public ResponseEntity<?> addRating(
            @PathVariable UUID id,
            @RequestParam Integer stars,
            @RequestParam(required = false) String comment,
            Authentication auth) {
        Toilet toilet = toiletRepo.findById(id).orElseThrow();
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        Rating rating = Rating.builder()
                .toilet(toilet).user(user)
                .stars(stars).comment(comment).build();
        ratingRepo.save(rating);
        return ResponseEntity.ok("Rating saved");
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<?> getRatings(@PathVariable UUID id) {
        Double avg = ratingRepo.findAvgStarsByToiletId(id);
        return ResponseEntity.ok(Map.of(
                "ratings", ratingRepo.findByToiletId(id),
                "average", avg != null ? avg : 0.0));
    }
}