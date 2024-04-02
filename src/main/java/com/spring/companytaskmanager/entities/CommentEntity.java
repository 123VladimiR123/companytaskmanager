package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
@Table(name = "comments")
public class CommentEntity{
    @Column(name = "post_or_task_id")
    @NotEmpty
    private String owner;
    @OneToOne
    @JoinColumn(name = "from_user")
    private UserInfoEntity fromUser;
    @Column(name = "date_published")
    private Timestamp published;
    @Id
    @Column(name = "comment_id")
    @NotEmpty
    private String commentId;
    @Column(name = "message")
    @NotEmpty
    @Size(max = 255, min = 3, message = "Комментарий должен содержать от 3 до 255 символов!")
    private String message;
    @OneToMany
    @JoinColumn(name = "post_task_comment_id", referencedColumnName = "comment_id")
    private Set<AttachmentEntity> attachmentEntitySet;
}
