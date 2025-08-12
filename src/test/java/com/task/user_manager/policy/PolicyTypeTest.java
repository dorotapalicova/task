package com.task.user_manager.policy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PolicyTypeTest {

    @Test
    void testEnumValues() {
        assertEquals("youngerThan", PolicyType.YOUNGER_THAN.getName());
        assertEquals("emailDomainIs", PolicyType.EMAIL_DOMAIN_IS.getName());
        assertEquals("isMemberOf", PolicyType.IS_MEMBER_OF.getName());
    }

    @Test
    void testEnumValueOf() {
        assertEquals(PolicyType.YOUNGER_THAN, PolicyType.valueOf("YOUNGER_THAN"));
        assertEquals(PolicyType.EMAIL_DOMAIN_IS, PolicyType.valueOf("EMAIL_DOMAIN_IS"));
        assertEquals(PolicyType.IS_MEMBER_OF, PolicyType.valueOf("IS_MEMBER_OF"));
    }

    @Test
    void testEnumNames() {
        assertEquals("YOUNGER_THAN", PolicyType.YOUNGER_THAN.name());
        assertEquals("EMAIL_DOMAIN_IS", PolicyType.EMAIL_DOMAIN_IS.name());
        assertEquals("IS_MEMBER_OF", PolicyType.IS_MEMBER_OF.name());
    }
}