package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.CommentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<CommentEntity, String> {
    public List<CommentEntity> findAllByOwner(String owner);
}
