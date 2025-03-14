package com.notifyme.repository;

import com.notifyme.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Notification entities.
 * Extends JpaRepository to provide CRUD operations and more.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
