package com.rebellion.notifyhub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notification_preferences")
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for jpa
public class NotificationPreference extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
    
    private boolean emailEnabled = true;
    
    private boolean pushEnabled = true;

    public NotificationPreference(User user) {
        this.user = user;
    }

    // SETTERS
    public void updateEmailPreference(boolean status){
        emailEnabled = status;
    }

    public void updatePushPreference(boolean status){
        pushEnabled = status;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
