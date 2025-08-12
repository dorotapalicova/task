package com.task.user_manager.service;

import com.task.user_manager.dto.policy.CreatePolicyRequest;
import com.task.user_manager.dto.policy.UpdatePolicyRequest;
import com.task.user_manager.exception.IllegalPolicyTypeException;
import com.task.user_manager.exception.PolicyAlreadyExistsException;
import com.task.user_manager.exception.PolicyNotFoundException;
import com.task.user_manager.exception.UserNotFoundException;
import com.task.user_manager.model.Policy;
import com.task.user_manager.policy.PolicyType;
import com.task.user_manager.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    private PolicyRepository policyRepository;
    private PolicyEvaluationService policyEvaluationService;
    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        policyRepository = mock(PolicyRepository.class);
        policyEvaluationService = mock(PolicyEvaluationService.class);
        policyService = new PolicyService(policyEvaluationService, policyRepository);
    }

    private Policy getPolicy(Map<String, String> conditions) {
        return new Policy("p1", "policy1", conditions);
    }

    @Test
    void findAll_shouldReturnPage() {
        List<Policy> policies = List.of(getPolicy(Map.of()));
        Page<Policy> page = new PageImpl<>(policies);
        when(policyRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Policy> result = policyService.findAll(0, 10);

        assertEquals(1, result.getTotalElements());
        assertEquals("p1", result.getContent().getFirst().getId());
    }

    @Test
    void find_shouldReturnPolicy() {
        Policy policy = getPolicy(Map.of());
        when(policyRepository.findById("policy1")).thenReturn(Optional.of(policy));

        Policy result = policyService.find("policy1");

        assertEquals("p1", result.getId());
    }

    @Test
    void find_shouldThrowIfNotFound() {
        when(policyRepository.findById("policy1")).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> policyService.find("policy1"));
    }

    @Test
    void create_shouldSaveAndReturnPolicy() {
        Map<String, String> conditions = Map.of(PolicyType.IS_MEMBER_OF.getName(), "IT");
        CreatePolicyRequest req = new CreatePolicyRequest("p1", "policy1", conditions);

        when(policyRepository.findById("policy1")).thenReturn(Optional.empty());

        Policy result = policyService.create(req);

        verify(policyEvaluationService).applyPolicyToAllUsers(any(Policy.class), eq(true));
        verify(policyRepository).save(any(Policy.class));
        assertEquals("p1", result.getId());
        assertEquals("policy1", result.getName());
        assertEquals(conditions, result.getConditions());
    }

    @Test
    void create_shouldThrowIfPolicyExists() {
        Map<String, String> conditions = Map.of(PolicyType.IS_MEMBER_OF.getName(), "IT");
        CreatePolicyRequest req = new CreatePolicyRequest("p1", "policy1", conditions);

        when(policyRepository.findById("policy1")).thenReturn(Optional.of(getPolicy(conditions)));

        assertThrows(PolicyAlreadyExistsException.class, () -> policyService.create(req));
    }

    @Test
    void create_shouldThrowIfInvalidConditionKey() {
        Map<String, String> conditions = Map.of("invalid_key", "value");
        CreatePolicyRequest req = new CreatePolicyRequest("p1", "policy1", conditions);

        when(policyRepository.findById("policy1")).thenReturn(Optional.empty());

        assertThrows(IllegalPolicyTypeException.class, () -> policyService.create(req));
    }

    @Test
    void update_shouldUpdateAndReturnPolicy() {
        Map<String, String> oldConditions = Map.of(PolicyType.IS_MEMBER_OF.getName(), "IT");
        Policy policy = getPolicy(oldConditions);

        Map<String, String> newConditions = Map.of(PolicyType.EMAIL_DOMAIN_IS.getName(), "example.com");
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setName("policy1");
        req.setConditions(newConditions);

        when(policyRepository.findById("policy1")).thenReturn(Optional.of(policy));

        Policy result = policyService.update("policy1", req);

        verify(policyEvaluationService).applyPolicyToAllUsers(policy, false);
        verify(policyRepository).save(policy);
        assertEquals(newConditions, result.getConditions());
    }

    @Test
    void update_shouldThrowIfNotFound() {
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        when(policyRepository.findById("policy1")).thenReturn(Optional.empty());

        assertThrows(PolicyNotFoundException.class, () -> policyService.update("policy1", req));
    }

    @Test
    void update_shouldThrowIfInvalidConditionKey() {
        Policy policy = getPolicy(Map.of());
        UpdatePolicyRequest req = new UpdatePolicyRequest();
        req.setConditions(Map.of("invalid_key", "value"));

        when(policyRepository.findById("policy1")).thenReturn(Optional.of(policy));

        assertThrows(IllegalPolicyTypeException.class, () -> policyService.update("policy1", req));
    }

    @Test
    void delete_shouldRemovePolicy() {
        Policy policy = getPolicy(Map.of());
        when(policyRepository.findById("policy1")).thenReturn(Optional.of(policy));

        policyService.delete("policy1");

        verify(policyRepository).delete(policy);
    }

    @Test
    void delete_shouldThrowIfNotFound() {
        when(policyRepository.findById("policy1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> policyService.delete("policy1"));
    }
}