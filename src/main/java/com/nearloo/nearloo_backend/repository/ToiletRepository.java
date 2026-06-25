package com.nearloo.nearloo_backend.repository;

import com.nearloo.nearloo_backend.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.UUID;

public interface ToiletRepository extends JpaRepository<Toilet, UUID> {

    // Haversine formula — nearby toilets within radius (km)
    @Query(value = """
            SELECT * FROM toilets
            WHERE (6371 * acos(
                cos(radians(:lat)) * cos(radians(latitude))
                * cos(radians(longitude) - radians(:lng))
                + sin(radians(:lat)) * sin(radians(latitude))
            )) < :radiusKm
            ORDER BY (6371 * acos(
                cos(radians(:lat)) * cos(radians(latitude))
                * cos(radians(longitude) - radians(:lng))
                + sin(radians(:lat)) * sin(radians(latitude))
            )) ASC
            """, nativeQuery = true)
    List<Toilet> findNearby(@Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusKm") double radiusKm);
}
