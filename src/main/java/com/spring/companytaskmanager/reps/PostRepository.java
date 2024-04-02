package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.PostEntity;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<PostEntity, String> {
}
