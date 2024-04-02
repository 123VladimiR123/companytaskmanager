package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.ChangeEmailEntity;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;

public interface EmailChangeRepository extends CrudRepository<ChangeEmailEntity, String> {

    @Query("select e from ChangeEmailEntity e where e.expiredTime > ?1 and e.oldEmail = ?2 and e.key = ?3")
    ChangeEmailEntity findChangeEmailEntityByExpiredTimeBeforeAndOldEmailAndKey(Timestamp time, String old_username, String key);
}
