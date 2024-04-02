package com.spring.companytaskmanager.reps;

import com.spring.companytaskmanager.entities.UserInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserInfoPagingAndSorting extends PagingAndSortingRepository<UserInfoEntity, String> {
    @Query(value = "SELECT * FROM user_info user " +
            "WHERE lower(concat(user.last_name, user.first_name, user.father_name, user.department)) REGEXP ?1",
            countQuery = "SELECT COUNT(*) FROM user_info user " +
                    "WHERE lower(concat(user.last_name, user.first_name, user.father_name, user.department)) REGEXP ?1",
            nativeQuery = true)
    public Page<UserInfoEntity> findByQuery(String query, Pageable pageable);
}
