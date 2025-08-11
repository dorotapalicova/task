package com.task.user_manager.policy.evaluator.impl;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.PolicyType;

public class EmailDomainIsEvaluator implements PolicyEvaluator<PolicyType> {
    @Override
    public PolicyType getPolicyType() {
        return PolicyType.EMAIL_DOMAIN_IS;
    }

    @Override
    public boolean evaluate(User user, Object conditionValue) {
        String domain = (String) conditionValue;
        return user.getEmailAddress().endsWith("@" + domain);
    }
}