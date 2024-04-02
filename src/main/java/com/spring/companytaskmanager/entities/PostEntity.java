package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "posts")
public class PostEntity {
    @Id
    @NotEmpty
    @Column(name = "post_id")
    private String postId;
    @OneToOne
    @JoinColumn(name = "from_user")
    private UserInfoEntity fromUser;
    @Column(name = "message")
    @NotEmpty
    private String message;
    @Column(name = "date_published")
    private Timestamp published;
    @OneToMany
    @JoinColumn(name = "post_task_comment_id", referencedColumnName = "post_id")
    private Set<AttachmentEntity> attachmentEntitySet;
    @Transient
    private Page<CommentEntity> commentsPreview;
}
