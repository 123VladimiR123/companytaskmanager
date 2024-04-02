package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.AttachmentEntity;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<AttachmentEntity, String> {
}
