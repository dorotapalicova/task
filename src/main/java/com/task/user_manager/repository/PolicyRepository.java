package com.task.user_manager.repository;

import com.task.user_manager.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PolicyRepository extends PagingAndSortingRepository<Policy, String>, JpaRepository<Policy, String> {
}
