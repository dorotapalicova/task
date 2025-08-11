package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;

public interface PolicyEvaluator<T extends Enum<T>> {
    PolicyType getPolicyType();

    boolean evaluate(User user, Object conditionValue);}
