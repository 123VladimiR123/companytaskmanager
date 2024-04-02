package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.TaskEntity;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<TaskEntity, String> {
}
