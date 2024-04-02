package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class TaskEntity {
    @Id
    @Column(name = "task_id")
    private String taskId;
    @OneToOne
    @JoinColumn(name = "for_user")
    private UserInfoEntity forUser;
    @OneToOne
    @JoinColumn(name = "from_user")
    private UserInfoEntity fromUser;
    @Column(name = "message")
    @NotEmpty
    private String message;
    @Column(name = "date_published")
    private Timestamp published;
    @OneToMany
    @JoinColumn(name = "post_task_comment_id", referencedColumnName = "task_id")
    private Set<AttachmentEntity> attachmentEntitySet;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(name = "name")
    private String name;
}
