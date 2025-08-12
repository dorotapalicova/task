package com.task.user_manager.policy.evaluator;

import com.task.user_manager.model.User;
import com.task.user_manager.policy.PolicyType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailDomainIsEvaluatorTest {

    @Test
    void getPolicyType_shouldReturnEmailDomainIs() {
        EmailDomainIsEvaluator evaluator = new EmailDomainIsEvaluator();
        assertEquals(PolicyType.EMAIL_DOMAIN_IS, evaluator.getPolicyType());
    }

    @Test
    void evaluate_shouldReturnTrueWhenDomainMatches() {
        User user = new User();
        user.setEmailAddress("alice@example.com");
        EmailDomainIsEvaluator evaluator = new EmailDomainIsEvaluator();

        assertTrue(evaluator.evaluate(user, "example.com"));
    }

    @Test
    void evaluate_shouldReturnFalseWhenDomainDoesNotMatch() {
        User user = new User();
        user.setEmailAddress("bob@other.com");
        EmailDomainIsEvaluator evaluator = new EmailDomainIsEvaluator();

        assertFalse(evaluator.evaluate(user, "example.com"));
    }

    @Test
    void evaluate_shouldReturnFalseWhenEmailIsNull() {
        User user = new User();
        user.setEmailAddress(null);
        EmailDomainIsEvaluator evaluator = new EmailDomainIsEvaluator();

        assertThrows(NullPointerException.class, () -> evaluator.evaluate(user, "example.com"));
    }
}