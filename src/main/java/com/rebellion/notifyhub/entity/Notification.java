package com.rebellion.notifyhub.entity;

import com.rebellion.notifyhub.entity.enums.NotificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for jpa
public class Notification extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;
    
    @Column(nullable = false)
    private String message;
    
    private String type;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.PENDING;

    public Notification(User user, String title, String message, String type) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.type = type;
    }

	// SETTERS
	public void markSent(){
		if(this.status != NotificationStatus.PENDING){
			throw new IllegalStateException("Invalid transition: " + this.status.name() + " --> SENT");
		}
		this.status = NotificationStatus.SENT;
	}

	public void markFailed(){
		if(this.status != NotificationStatus.PENDING){
			throw new IllegalStateException("Invalid transition: " + this.status.name() + " --> FAILED");
		}
		this.status = NotificationStatus.FAILED;
	}

	public void markRead(){
		if(this.status != NotificationStatus.SENT){
			throw new IllegalStateException("Invalid transition: " + this.status.name() + " --> READ");
		}
		this.status = NotificationStatus.READ;
	}
}
