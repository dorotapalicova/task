package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;

import java.time.LocalDate;
import java.time.Period;

public class YoungerThanEvaluator implements PolicyEvaluator {

    @Override
    public PolicyType getPolicyType() {
        return PolicyType.YOUNGER_THAN;
    }

    @Override
    public boolean evaluate(User user, String conditionValue) {
        int ageLimit = Integer.parseInt(conditionValue);
        int userAge = Period.between(user.getBirthDate(), LocalDate.now()).getYears();
        return userAge < ageLimit;
    }
}
