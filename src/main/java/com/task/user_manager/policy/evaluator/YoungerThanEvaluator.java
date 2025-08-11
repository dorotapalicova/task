package com.task.user_manager.policy.evaluator.impl;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.evaluator.PolicyEvaluator;
import com.task.user_manager.policy.PolicyType;

import java.time.LocalDate;
import java.time.Period;

public class YoungerThanEvaluator implements PolicyEvaluator<PolicyType> {

    @Override
    public PolicyType getPolicyType() {
        return PolicyType.YOUNGER_THAN;
    }

    @Override
    public boolean evaluate(User user, Object conditionValue) {
        int ageLimit = (Integer) conditionValue;
        int userAge = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        return userAge < ageLimit;
    }
}
