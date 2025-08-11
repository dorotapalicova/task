package com.task.user_manager.repository;

import com.task.user_manager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, String>, JpaRepository<User, String> {

    Optional<User> findUserByName(String name);
}
