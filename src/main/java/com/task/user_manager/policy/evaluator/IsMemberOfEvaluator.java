package com.task.user_manager.policy.evaluator.impl;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.PolicyType;

public class IsMemberOfEvaluator implements PolicyEvaluator<PolicyType> {
    @Override
    public PolicyType getPolicyType() {
        return PolicyType.IS_MEMBER_OF;
    }

    @Override
    public boolean evaluate(User user, Object conditionValue) {
        String accessGroup = (String) conditionValue;
        return user.getOrganizationUnit().contains(accessGroup);
    }
}