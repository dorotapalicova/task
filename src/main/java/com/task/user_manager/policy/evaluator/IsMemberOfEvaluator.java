package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;

public class IsMemberOfEvaluator implements PolicyEvaluator {
    @Override
    public PolicyType getPolicyType() {
        return PolicyType.IS_MEMBER_OF;
    }

    @Override
    public boolean evaluate(User user, String conditionValue) {
        return user.getOrganizationUnit().contains(conditionValue);
    }
}