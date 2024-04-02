package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TasksPagingAndSorting extends PagingAndSortingRepository<TaskEntity, String> {
    @Query(value = "SELECT * FROM tasks t WHERE t.for_user = ?1 AND lower(t.name) REGEXP ?2",
            countQuery = "SELECT COUNT(*) FROM tasks t WHERE t.for_user = ?1 AND lower(t.name) REGEXP ?2",
            nativeQuery = true)
    public Page<TaskEntity> findByQueryToUser(String forUser, String query, Pageable pageable);

    /**
     * MUST BE IN 'quotes'
     */
    @Query(value = "SELECT * FROM tasks t WHERE t.from_user = ?1 AND lower(t.name) REGEXP ?2",
            countQuery = "SELECT COUNT(*) FROM tasks t WHERE t.from_user = ?1 AND lower(t.name) REGEXP ?2",
            nativeQuery = true)
    public Page<TaskEntity> findByQueryFromUser(String username, String query, Pageable pageable);

    @Query(value = "SELECT * FROM tasks t WHERE lower(t.name) REGEXP ?1",
            countQuery = "SELECT COUNT(*) FROM tasks t WHERE lower(t.name) REGEXP ?1",
            nativeQuery = true)
    public Page<TaskEntity> findByQuery(String taskName, Pageable pageable);

    public TaskEntity findByTaskId(String id);
}
