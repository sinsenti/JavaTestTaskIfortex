package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query(value = """
             SELECT * FROM sessions
             WHERE device_type = :deviceType
             ORDER BY started_at_utc ASC
             LIMIT 1
            """, nativeQuery = true)
    Session getFirstDesktopSession(DeviceType deviceType);
//    SELECT s.id, s.device_type, s.ended_at_utc, s.started_at_utc, s.user_id
//    FROM sessions s
//    JOIN users u ON s.user_id = u.id
//    WHERE u.deleted = FALSE
//    AND s.ended_at_utc IS NOT NULL
//    AND s.ended_at_utc < :endDate
//    ORDER BY s.started_at_utc DESC

    @Query(value = """
            SELECT s.* FROM sessions s
            JOIN users u ON s.user_id = u.id
            WHERE u.deleted = FALSE
            AND s.ended_at_utc IS NOT NULL
            AND s.ended_at_utc < :endDate
            ORDER BY s.started_at_utc DESC
            """, nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(@Param("endDate") LocalDateTime endDate);
}
