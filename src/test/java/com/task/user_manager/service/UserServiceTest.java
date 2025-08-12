package com.task.user_manager.service;

import com.task.user_manager.dto.user.CreateUserRequest;
import com.task.user_manager.dto.user.UpdateUserRequest;
import com.task.user_manager.exception.UserAlreadyExistsException;
import com.task.user_manager.exception.UserNotFoundException;
import com.task.user_manager.model.User;
import com.task.user_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private PolicyEvaluationService policyEvaluationService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        policyEvaluationService = mock(PolicyEvaluationService.class);
        userService = new UserService(userRepository, policyEvaluationService);
    }

    private User getUser() {
        User user = new User();
        user.setName("user1");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmailAddress("user1" + "@example.com");
        user.setOrganizationUnit(List.of("IT"));
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setRegisteredOn(LocalDate.now());
        user.setPolicy(new HashSet<>());
        return user;
    }

    @Test
    void create_shouldSaveAndReturnUser() {
        CreateUserRequest req = new CreateUserRequest(
                "user1", "First", "Last", "user1@example.com", List.of("IT"), LocalDate.of(2000, 1, 1)
        );
        when(userRepository.findUserByName("user1")).thenReturn(Optional.empty());
        User user = getUser();
        when(policyEvaluationService.applyPolicies(any(User.class))).thenReturn(user);

        User result = userService.create(req);

        verify(userRepository).save(any(User.class));
        assertEquals("user1", result.getName());
    }

    @Test
    void create_shouldThrowIfUserExists() {
        CreateUserRequest req = new CreateUserRequest(
                "user1", "First", "Last", "user1@example.com", List.of("IT"), LocalDate.of(2000, 1, 1)
        );
        when(userRepository.findUserByName("user1")).thenReturn(Optional.of(getUser()));

        assertThrows(UserAlreadyExistsException.class, () -> userService.create(req));
    }

    @Test
    void find_shouldReturnUser() {
        User user = getUser();
        when(userRepository.findUserByName("user1")).thenReturn(Optional.of(user));

        User result = userService.find("user1");

        assertEquals("user1", result.getName());
    }

    @Test
    void find_shouldThrowIfNotFound() {
        when(userRepository.findUserByName("user1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.find("user1"));
    }

    @Test
    void update_shouldUpdateFieldsAndSave() {
        User user = getUser();
        when(userRepository.findUserByName("user1")).thenReturn(Optional.of(user));
        when(policyEvaluationService.applyPolicies(any(User.class))).thenReturn(user);

        UpdateUserRequest req = new UpdateUserRequest();
        req.setFirstName("NewFirst");
        req.setLastName("NewLast");
        req.setEmailAddress("new@example.com");
        req.setOrganizationUnit(List.of("HR"));
        req.setBirthDate(LocalDate.of(1995, 5, 5));

        User result = userService.update("user1", req);

        verify(userRepository).save(user);
        assertEquals("NewFirst", result.getFirstName());
        assertEquals("NewLast", result.getLastName());
        assertEquals("new@example.com", result.getEmailAddress());
        assertEquals(List.of("HR"), result.getOrganizationUnit());
        assertEquals(LocalDate.of(1995, 5, 5), result.getBirthDate());
    }

    @Test
    void update_shouldThrowIfNotFound() {
        when(userRepository.findUserByName("user1")).thenReturn(Optional.empty());
        UpdateUserRequest req = new UpdateUserRequest();

        assertThrows(UserNotFoundException.class, () -> userService.update("user1", req));
    }

    @Test
    void delete_shouldRemoveUser() {
        User user = getUser();
        when(userRepository.findUserByName("user1")).thenReturn(Optional.of(user));

        userService.delete("user1");

        verify(userRepository).delete(user);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(userRepository.findUserByName("user1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete("user1"));
    }
}