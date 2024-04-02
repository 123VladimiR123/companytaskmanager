package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.DepartmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentPagingAndSorting extends PagingAndSortingRepository<DepartmentEntity, String> {

    @Query(value = "SELECT * FROM departments dep WHERE lower(dep.department_name) REGEXP ?1",
            countQuery = "SELECT COUNT(*) FROM departments dep WHERE lower(dep.department_name) REGEXP ?1",
            nativeQuery = true)
    Page<DepartmentEntity> findByQuery(String query, PageRequest pageRequest);
}
