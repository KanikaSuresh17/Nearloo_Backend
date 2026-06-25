package com.nearloo.nearloo_backend.repository;

import com.nearloo.nearloo_backend.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    List<Rating> findByToiletId(UUID toiletId);

    @Query("SELECT AVG(r.stars) FROM Rating r WHERE r.toilet.id = :toiletId")
    Double findAvgStarsByToiletId(@Param("toiletId") UUID toiletId);
}
