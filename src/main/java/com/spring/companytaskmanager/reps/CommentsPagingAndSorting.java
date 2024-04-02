package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentsPagingAndSorting extends PagingAndSortingRepository<CommentEntity, String> {

    public Page<CommentEntity> findCommentEntitiesByOwner(String previewTo, Pageable pageable);
    public Page<CommentEntity> findAllByOwner(String owner, Pageable pageable);
}
