package com.task.user_manager.service;

import com.task.user_manager.dto.user.CreateUserRequest;
import com.task.user_manager.dto.user.UpdateUserRequest;
import com.task.user_manager.exception.UserAlreadyExistsException;
import com.task.user_manager.exception.UserNotFoundException;
import com.task.user_manager.model.User;
import com.task.user_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PolicyEvaluationService policyEvaluationService;

    public Page<User> findAll(int page, int length) {
        return this.userRepository.findAll(PageRequest.of(page, length));
    }

    public User find(String name) throws UserNotFoundException {
        return this.userRepository.findUserByName(name).orElseThrow(() -> new UserNotFoundException(name));
    }

    public User create(CreateUserRequest request) {
        if (this.userRepository.findUserByName(request.getName()).isPresent()) {
            throw new UserAlreadyExistsException(request.getName());
        }

        User user = new User(
                request.getName(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmailAddress(),
                request.getOrganizationUnit(),
                request.getBirthDate(),
                LocalDate.now(),
                new HashSet<>());
        user = policyEvaluationService.applyPolicies(user);

        userRepository.save(user);
        return user;
    }

    public User update(String name, UpdateUserRequest request) throws UserNotFoundException {
        User user = this.userRepository.findUserByName(name).orElseThrow(() -> new UserNotFoundException(name));

        Optional.ofNullable(request.getFirstName()).ifPresent(user::setFirstName);
        Optional.ofNullable(request.getLastName()).ifPresent(user::setLastName);
        Optional.ofNullable(request.getEmailAddress()).ifPresent(user::setEmailAddress);
        Optional.ofNullable(request.getOrganizationUnit()).ifPresent(user::setOrganizationUnit);
        Optional.ofNullable(request.getBirthDate()).ifPresent(user::setBirthDate);

        user = policyEvaluationService.applyPolicies(user);

        userRepository.save(user);
        return user;
    }

    public void delete(String name) throws UserNotFoundException {
        userRepository.delete(this.userRepository.findUserByName(name)
                .orElseThrow(() -> new UserNotFoundException(name)));
    }
}
