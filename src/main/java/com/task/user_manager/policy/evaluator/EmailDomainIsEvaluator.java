package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;

public class EmailDomainIsEvaluator implements PolicyEvaluator {
    @Override
    public PolicyType getPolicyType() {
        return PolicyType.EMAIL_DOMAIN_IS;
    }

    @Override
    public boolean evaluate(User user, String conditionValue) {
        return user.getEmailAddress().endsWith("@" + conditionValue);
    }
}