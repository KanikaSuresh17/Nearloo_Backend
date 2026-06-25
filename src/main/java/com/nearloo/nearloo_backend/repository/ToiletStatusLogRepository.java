package com.nearloo.nearloo_backend.repository;

import com.nearloo.nearloo_backend.entity.ToiletStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ToiletStatusLogRepository extends JpaRepository<ToiletStatusLog, UUID> {
    List<ToiletStatusLog> findByToiletIdOrderByChangedAtDesc(UUID toiletId);
}