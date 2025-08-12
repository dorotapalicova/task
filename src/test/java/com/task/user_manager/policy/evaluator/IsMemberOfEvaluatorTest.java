package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IsMemberOfEvaluatorTest {

    @Test
    void getPolicyType_shouldReturnIsMemberOf() {
        IsMemberOfEvaluator evaluator = new IsMemberOfEvaluator();
        assertEquals(PolicyType.IS_MEMBER_OF, evaluator.getPolicyType());
    }

    @Test
    void evaluate_shouldReturnTrueWhenUserIsMember() {
        User user = new User();
        user.setOrganizationUnit(List.of("IT", "HR"));
        IsMemberOfEvaluator evaluator = new IsMemberOfEvaluator();

        assertTrue(evaluator.evaluate(user, "IT"));
    }

    @Test
    void evaluate_shouldReturnFalseWhenUserIsNotMember() {
        User user = new User();
        user.setOrganizationUnit(List.of("Finance", "HR"));
        IsMemberOfEvaluator evaluator = new IsMemberOfEvaluator();

        assertFalse(evaluator.evaluate(user, "IT"));
    }

    @Test
    void evaluate_shouldThrowExceptionWhenOrganizationUnitIsNull() {
        User user = new User();
        user.setOrganizationUnit(null);
        IsMemberOfEvaluator evaluator = new IsMemberOfEvaluator();

        assertThrows(NullPointerException.class, () -> evaluator.evaluate(user, "IT"));
    }
}