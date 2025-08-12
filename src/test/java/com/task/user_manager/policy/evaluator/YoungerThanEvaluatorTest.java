package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class YoungerThanEvaluatorTest {

    @Test
    void getPolicyType_shouldReturnYoungerThan() {
        YoungerThanEvaluator evaluator = new YoungerThanEvaluator();
        assertEquals(PolicyType.YOUNGER_THAN, evaluator.getPolicyType());
    }

    @Test
    void evaluate_shouldReturnTrueWhenUserIsYounger() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(20));
        YoungerThanEvaluator evaluator = new YoungerThanEvaluator();

        assertTrue(evaluator.evaluate(user, "25"));
    }

    @Test
    void evaluate_shouldReturnFalseWhenUserIsOlder() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(30));
        YoungerThanEvaluator evaluator = new YoungerThanEvaluator();

        assertFalse(evaluator.evaluate(user, "25"));
    }

    @Test
    void evaluate_shouldThrowExceptionWhenBirthDateIsNull() {
        User user = new User();
        user.setBirthDate(null);
        YoungerThanEvaluator evaluator = new YoungerThanEvaluator();

        assertThrows(NullPointerException.class, () -> evaluator.evaluate(user, "25"));
    }

    @Test
    void evaluate_shouldThrowExceptionWhenConditionValueIsNotANumber() {
        User user = new User();
        user.setBirthDate(LocalDate.now().minusYears(20));
        YoungerThanEvaluator evaluator = new YoungerThanEvaluator();

        assertThrows(NumberFormatException.class, () -> evaluator.evaluate(user, "abc"));
    }
}