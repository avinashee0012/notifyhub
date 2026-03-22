package com.rebellion.notifyhub.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
	name = "notification_events",
	uniqueConstraints = @UniqueConstraint(columnNames = "event_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for jpa
public class NotificationEvent extends BaseEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "event_id", nullable = false)
    private Long eventId; // for idempotency

    @Column(nullable = false)
    private String eventType;
	
	@Column(nullable = false)
	private Long userId;

    @Lob
    @Column(nullable = false)
    private String payload;

    public NotificationEvent(String eventType, Long eventId, Long userId, String payload) {
        this.eventType = eventType;
		this.eventId = eventId;
		this.userId = userId;
        this.payload = payload;
    }
}
