package com.task.user_manager.policy.factory;

import com.task.user_manager.policy.PolicyType;
import com.task.user_manager.policy.evaluator.EmailDomainIsEvaluator;
import com.task.user_manager.policy.evaluator.IsMemberOfEvaluator;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.evaluator.YoungerThanEvaluator;

import java.util.HashMap;
import java.util.Map;

public class PolicyEvaluatorFactory {
    private final Map<PolicyType, PolicyEvaluator> evaluators = new HashMap<>();

    public PolicyEvaluatorFactory() {
        registerEvaluator(new YoungerThanEvaluator());
        registerEvaluator(new EmailDomainIsEvaluator());
        registerEvaluator(new IsMemberOfEvaluator());
    }

    public void registerEvaluator(PolicyEvaluator evaluator) {
        evaluators.put(evaluator.getPolicyType(), evaluator);
    }

    public PolicyEvaluator getEvaluator(PolicyType policyType) {
        return evaluators.get(policyType);
    }
}
