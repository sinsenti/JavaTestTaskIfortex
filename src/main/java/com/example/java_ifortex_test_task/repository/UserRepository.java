package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
            SELECT u.*
            FROM users u
            JOIN (
                SELECT s.user_id, MAX(s.started_at_utc) AS latest_session_start
                FROM sessions s
                WHERE s.device_type = :deviceType
                GROUP BY s.user_id
            ) AS user_latest_sessions ON u.id = user_latest_sessions.user_id
            ORDER BY user_latest_sessions.latest_session_start DESC
            """, nativeQuery = true)
    List<User> getUsersWithAtLeastOneMobileSession(@Param("deviceType") int deviceType);

    @Query(value = """
            SELECT u.* FROM users u
            JOIN (
                SELECT user_id, COUNT(*) as session_count
                FROM sessions
                GROUP BY user_id
                ORDER BY session_count DESC
                LIMIT 1
            ) sub ON u.id = sub.user_id
            """, nativeQuery = true)
    User getUserWithMostSessions();

}
