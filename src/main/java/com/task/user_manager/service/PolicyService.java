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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyEvaluationService policyEvaluationService;
    private final PolicyRepository policyRepository;


    public Page<Policy> findAll(int page, int length) {
        return this.policyRepository.findAll(PageRequest.of(page, length));
    }

    public Policy find(@Valid String name) throws PolicyNotFoundException {
        return this.policyRepository.findById(name).orElseThrow(() -> new PolicyNotFoundException(name));
    }

    public Policy create(CreatePolicyRequest request) throws IllegalPolicyTypeException {
        if (this.policyRepository.findById(request.getName()).isPresent()) {
            throw new PolicyAlreadyExistsException(request.getName());
        }

        this.checkValidConditions(request.getConditions());

        Policy policy = new Policy(
                request.getId(),
                request.getName(),
                request.getConditions());

        policyEvaluationService.applyPolicyToAllUsers(policy, true);

        policyRepository.save(policy);
        return policy;
    }

    public Policy update(String name, UpdatePolicyRequest request) throws IllegalPolicyTypeException, PolicyNotFoundException {
        if (request.getConditions() != null && !request.getConditions().isEmpty()) {
            this.checkValidConditions(request.getConditions());
        }

        Policy policy = this.policyRepository.findById(name).orElseThrow(() -> new PolicyNotFoundException(name));

        Optional.ofNullable(request.getName()).ifPresent(policy::setName);
        Optional.ofNullable(request.getConditions()).ifPresent(policy::setConditions);

        policyEvaluationService.applyPolicyToAllUsers(policy, false);

        policyRepository.save(policy);
        return policy;
    }

    public void delete(String name) {
        policyRepository.delete(this.policyRepository.findById(name)
                .orElseThrow(() -> new UserNotFoundException(name)));
    }

    private void checkValidConditions(Map<String, String> conditions) throws IllegalPolicyTypeException {
        Set<String> allowedKeys = Arrays.stream(PolicyType.values())
                .map(PolicyType::getName)
                .collect(Collectors.toSet());

        boolean allKeysValid = allowedKeys.containsAll(conditions.keySet());

        if (!allKeysValid) {
            throw new IllegalPolicyTypeException(allowedKeys);
        }
    }
}
