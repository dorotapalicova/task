package com.task.user_manager.service;

import com.task.user_manager.exception.PolicyNotFoundException;
import com.task.user_manager.model.Policy;
import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.factory.PolicyEvaluatorFactory;
import com.task.user_manager.repository.PolicyRepository;
import com.task.user_manager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PolicyEvaluationService {
    private final PolicyEvaluatorFactory evaluatorFactory = new PolicyEvaluatorFactory();
    private final PolicyRepository policyRepository;
    private final UserRepository userRepository;

    public void applyPolicyToAllUsers(Policy policy, boolean isNewPolicy) throws PolicyNotFoundException {
        if (isNewPolicy) {
            userRepository.findAll().forEach(user -> {
                if (doesPolicyApplyToUser(policy, user)) {
                    Set<String> usersPolicies = user.getPolicy();
                    usersPolicies.add(policy.getId());
                    user.setPolicy(usersPolicies);
                    userRepository.save(user);
                }
            });
        } else {
            userRepository.findAll().forEach(user -> {
                Set<String> usersPolicies = user.getPolicy();
                if (doesPolicyApplyToUser(policy, user)) {
                    usersPolicies.add(policy.getId());
                    user.setPolicy(usersPolicies);
                    userRepository.save(user);
                } else if (usersPolicies.contains(policy.getId())) { // rule does no longer apply to user
                    usersPolicies.remove(policy.getId());
                    user.setPolicy(usersPolicies);
                    userRepository.save(user);
                }
            });
        }
    }

    public User applyPolicies(User user) {
        Set<String> applicablePolicies = policyRepository.findAll().stream()
                .filter(policy -> doesPolicyApplyToUser(policy, user))
                .map(Policy::getName)
                .collect(Collectors.toSet());

        user.setPolicy(applicablePolicies);
        return user;
    }

    private boolean doesPolicyApplyToUser(Policy policy, User user) {
        for (Map.Entry<String, String> conditionEntry : policy.getConditions().entrySet()) {
            String conditionKey = conditionEntry.getKey();
            Object conditionValue = conditionEntry.getValue();

            PolicyType policyType = Arrays.stream(PolicyType.values())
                    .filter(pt -> pt.getName().equals(conditionKey))
                    .findFirst()
                    .orElse(null);

            if (policyType == null) {
                throw new IllegalArgumentException("Invalid policy condition key: " + conditionKey);
            }

            PolicyEvaluator<?> evaluator = evaluatorFactory.getEvaluator(policyType);
            if (evaluator == null || !evaluator.evaluate(user, conditionValue)) {
                return false;
            }
        }
        return true;
    }
}
