package com.task.user_manager.service;

import com.task.user_manager.model.Policy;
import com.task.user_manager.model.User;
import com.task.user_manager.repository.PolicyRepository;
import com.task.user_manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PolicyEvaluationServiceTest {

    private PolicyRepository policyRepository;
    private UserRepository userRepository;
    private PolicyEvaluationService service;

    @BeforeEach
    void setUp() {
        policyRepository = mock(PolicyRepository.class);
        userRepository = mock(UserRepository.class);
        service = new PolicyEvaluationService(policyRepository, userRepository);
    }

    private User getUser(String name) {
        User user = new User();
        user.setName(name);
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmailAddress(name + "@example.com");
        user.setOrganizationUnit(List.of("IT"));
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        user.setRegisteredOn(LocalDate.now());
        user.setPolicy(new HashSet<>());
        return user;
    }

    private Policy getPolicy(String id, String name, Map<String, String> conditions) {
        return new Policy(id, name, conditions);
    }

    @Test
    void applyPolicyToAllUsers_shouldAddPolicyToMatchingUsers_whenIsNewPolicy() {
        User user1 = getUser("user1");
        User user2 = getUser("user2");
        List<User> users = Arrays.asList(user1, user2);

        Map<String, String> conditions = new HashMap<>();
        conditions.put("isMemberOf", "IT");
        Policy policy = getPolicy("p1", "policy1", conditions);

        when(userRepository.findAll()).thenReturn(users);

        service.applyPolicyToAllUsers(policy, true);

        verify(userRepository, times(2)).save(any(User.class));
        assertTrue(user1.getPolicy().contains("p1"));
        assertTrue(user2.getPolicy().contains("p1"));
    }

    @Test
    void applyPolicyToAllUsers_shouldAddAndRemovePolicy_whenIsNotNewPolicy() {
        User user1 = getUser("user1");
        user1.getPolicy().add("p1");
        User user2 = getUser("user2");
        user2.getPolicy().add("p1");
        List<User> users = Arrays.asList(user1, user2);

        Map<String, String> conditions = new HashMap<>();
        conditions.put("emailDomainIs", "example.com");
        Policy policy = getPolicy("p1", "policy1", conditions);

        when(userRepository.findAll()).thenReturn(users);

        service.applyPolicyToAllUsers(policy, false);

        verify(userRepository, times(2)).save(any(User.class));
        assertTrue(user1.getPolicy().contains("p1"));
        assertTrue(user2.getPolicy().contains("p1"));

        // Now, change policy so no user matches
        Map<String, String> newConditions = new HashMap<>();
        newConditions.put("emailDomainIs", "other.com");
        Policy policy2 = getPolicy("p1", "policy1", newConditions);

        service.applyPolicyToAllUsers(policy2, false);

        verify(userRepository, times(4)).save(any(User.class));
        assertFalse(user1.getPolicy().contains("p1"));
        assertFalse(user2.getPolicy().contains("p1"));
    }

    @Test
    void applyPolicies_shouldSetApplicablePolicies() {
        User user = getUser("user1");
        Map<String, String> cond1 = new HashMap<>();
        cond1.put("isMemberOf", "IT");
        Policy policy1 = getPolicy("p1", "policy1", cond1);

        Map<String, String> cond2 = new HashMap<>();
        cond2.put("emailDomainIs", "other.com");
        Policy policy2 = getPolicy("p2", "policy2", cond2);

        when(policyRepository.findAll()).thenReturn(Arrays.asList(policy1, policy2));

        User result = service.applyPolicies(user);

        assertTrue(result.getPolicy().contains("policy1"));
        assertFalse(result.getPolicy().contains("policy2"));
    }

    @Test
    void doesPolicyApplyToUser_shouldThrowForInvalidConditionKey() {
        User user = getUser("user1");
        Map<String, String> cond = new HashMap<>();
        cond.put("invalid_key", "value");
        Policy policy = getPolicy("p1", "policy1", cond);

        assertThrows(IllegalArgumentException.class, () -> {
            try {
                var m = PolicyEvaluationService.class.getDeclaredMethod("doesPolicyApplyToUser", Policy.class, User.class);
                m.setAccessible(true);
                m.invoke(service, policy, user);
            } catch (Exception e) {
                throw e.getCause();
            }
        });
    }
}