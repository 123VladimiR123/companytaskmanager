package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "change_email")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChangeEmailEntity {
    @Id
    @Column(name = "old_username")
    @Email
    private String oldEmail;
    @Column(name = "new_username")
    @Email
    private String newEmail;
    @Column(name = "expired_time")
    private Timestamp expiredTime;
    @Column(name = "verification_key")
    private String key;
    @OneToOne
    @JoinColumn(name = "old_username", referencedColumnName = "username")
    private UserInfoEntity user;
}
