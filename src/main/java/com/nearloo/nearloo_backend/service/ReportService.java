package com.nearloo.nearloo_backend.service;

import com.nearloo.nearloo_backend.entity.*;
import com.nearloo.nearloo_backend.repository.*;
import com.nearloo.nearloo_backend.websocket.ToiletStatusHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReportService {

        private final ReportRepository reportRepo;
        private final ToiletRepository toiletRepo;
        private final UserRepository userRepo;
        private final NotificationService notificationService;
        private final ToiletStatusHandler statusHandler;
        private final ToiletStatusLogRepository statusLogRepo;

        private static final int DIRTY_THRESHOLD = 5; // 5+ reports → auto dirty

        public String submitReport(UUID toiletId, UUID userId, String reportType) {

                // Duplicate report check
                if (reportRepo.existsByToiletIdAndUserId(toiletId, userId)) {
                        return "Already reported";
                }

                Toilet toilet = toiletRepo.findById(toiletId)
                                .orElseThrow(() -> new RuntimeException("Toilet not found"));

                User user = userRepo.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Save report
                ToiletReport report = ToiletReport.builder()
                                .toilet(toilet)
                                .user(user)
                                .reportType(reportType)
                                .build();

                reportRepo.save(report);

                statusHandler.broadcastStatusUpdate(
                                toiletId.toString(),
                                "dirty");

                // Majority voting — if dirty reports >= threshold
                if ("dirty".equalsIgnoreCase(reportType)) {

                        long dirtyCount = reportRepo.countByToiletAndReportType(
                                        toilet,
                                        "dirty");

                        if (dirtyCount >= DIRTY_THRESHOLD
                                        && !"dirty".equalsIgnoreCase(toilet.getStatus())) {

                                String oldStatus = toilet.getStatus();

                                // Update toilet status
                                toilet.setStatus("dirty");
                                toiletRepo.save(toilet);

                                // Save status log
                                ToiletStatusLog log = ToiletStatusLog.builder()
                                                .toilet(toilet)
                                                .changedBy(null) // System change
                                                .oldStatus(oldStatus)
                                                .newStatus("dirty")
                                                .build();

                                statusLogRepo.save(log);

                                // WebSocket update
                                statusHandler.broadcastStatusUpdate(
                                                toiletId.toString(),
                                                "dirty");

                                // Notification to admins
                                notificationService.notifyAdmin(
                                                toilet,
                                                "Toilet '" + toilet.getName()
                                                                + "' marked DIRTY — "
                                                                + dirtyCount + " reports");
                        }
                }

                return "Report submitted";
        }
}