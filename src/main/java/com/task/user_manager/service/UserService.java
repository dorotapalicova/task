package com.task.user_manager.service;

import com.task.user_manager.dto.user.CreateUserRequest;
import com.task.user_manager.dto.user.UpdateUserRequest;
import com.task.user_manager.model.User;
import com.task.user_manager.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<User> findAll(int page, int length) {
        return this.userRepository.findAll(PageRequest.of(page, length));
    }

    public User find(String name) {
        //TODO
        return this.userRepository.findUserByName(name).orElseThrow(new ClassNotFoundException());
    }

    public User create(@Valid CreateUserRequest request) {
    }

    public Object update(Long id, @Valid UpdateUserRequest request) {
    }

    public void delete(Long id) {

    }
}
