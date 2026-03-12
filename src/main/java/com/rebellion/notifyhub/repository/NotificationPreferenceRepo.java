package com.rebellion.notifyhub.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rebellion.notifyhub.entity.NotificationPreference;

@Repository
public interface NotificationPreferenceRepo extends JpaRepository<NotificationPreference, Long>{   
    Optional<NotificationPreference> findByUserId(Long userId);
}
