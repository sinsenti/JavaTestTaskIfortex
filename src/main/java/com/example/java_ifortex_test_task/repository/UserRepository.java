package com.example.java_ifortex_test_task.repository;

import com.example.java_ifortex_test_task.entity.DeviceType;
import com.example.java_ifortex_test_task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = """
    SELECT DISTINCT u.* FROM users u
    JOIN sessions s ON u.id = s.user_id
    WHERE s.device_type = :deviceType
    ORDER BY s.started_at_utc DESC
    """, nativeQuery = true)
    List<User> getUsersWithAtLeastOneMobileSession(DeviceType deviceType);

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
