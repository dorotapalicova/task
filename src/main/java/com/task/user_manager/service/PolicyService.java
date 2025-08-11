package com.task.user_manager.service;

import com.task.user_manager.model.Policy;
import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.factory.PolicyEvaluatorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PolicyService {
    private final PolicyEvaluatorFactory evaluatorFactory = new PolicyEvaluatorFactory();

    public boolean doesPolicyApplyToUser(Policy policy, User user) {
        for (Map.Entry<String, Object> conditionEntry : policy.getConditions().entrySet()) {
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
