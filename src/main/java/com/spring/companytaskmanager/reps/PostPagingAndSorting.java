package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PostPagingAndSorting extends PagingAndSortingRepository<PostEntity, String> {

    @Query(value = "SELECT * FROM posts p WHERE lower(p.message) REGEXP ?1",
            countQuery = "SELECT COUNT(*) FROM posts p WHERE lower(p.message) REGEXP ?1",
            nativeQuery = true)
    public Page<PostEntity> findByQuery(String query, Pageable pageable);
}
