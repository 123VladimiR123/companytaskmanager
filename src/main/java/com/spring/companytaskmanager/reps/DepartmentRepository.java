package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.DepartmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, String> {
    List<DepartmentEntity> findAllByEnabledIsTrue();
}
