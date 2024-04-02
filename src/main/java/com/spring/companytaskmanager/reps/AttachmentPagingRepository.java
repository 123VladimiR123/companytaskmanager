package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.AttachmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AttachmentPagingRepository extends PagingAndSortingRepository<AttachmentEntity, String> {

    @Query(value = "SELECT * FROM attachments attach WHERE lower(attach.attachment_name) REGEXP ?1",
    countQuery = "SELECT COUNT(*) FROM attachments attach WHERE lower(attach.attachment_name) REGEXP ?1",
    nativeQuery = true)
    public Page<AttachmentEntity> findByQuery(String query, Pageable pageable);
}
