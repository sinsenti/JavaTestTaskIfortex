package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = """
    SELECT s.* FROM sessions s
    JOIN users u ON s.user_id = u.id
    WHERE s.ended_at_utc < :endDate 
    OR s.ended_at_utc == NULL
    ORDER BY s.started_at_utc DESC
    """, nativeQuery = true)
    List<Session> getSessionsFromActiveUsersEndedBefore2025(LocalDateTime endDate);
}
