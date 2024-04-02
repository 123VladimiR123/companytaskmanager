package com.spring.companytaskmanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attachments")
public class AttachmentEntity {
    @Column(name = "post_task_comment_id")
    @NotEmpty(message = "Совсем уже ку-ку")
    private String owner_id;
    @Column(name = "attachment_name")
    @NotEmpty(message = "Имя файла не может быть пустым")
    private String name;
    @Id
    @Column(name = "path")
    @NotEmpty
    private String path;
}
