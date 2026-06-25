package com.nearloo.nearloo_backend.repository;

import com.nearloo.nearloo_backend.entity.ToiletReport;
import com.nearloo.nearloo_backend.entity.Toilet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReportRepository extends JpaRepository<ToiletReport, UUID> {

    // Count dirty reports for a toilet
    long countByToiletAndReportType(Toilet toilet, String reportType);

    // Check if user already reported this toilet today
    boolean existsByToiletIdAndUserId(UUID toiletId, UUID userId);
}
