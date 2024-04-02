package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.UserInfoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserInfoRepository extends CrudRepository<UserInfoEntity, String> {
    UserInfoEntity findByFirstNameAndLastNameAndFatherName(String firstName, String lastName, String fatherName);
}
