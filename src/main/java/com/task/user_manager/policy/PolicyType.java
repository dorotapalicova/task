package com.task.user_manager.policy;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PolicyType {
    YOUNGER_THAN("youngerThan"),
    EMAIL_DOMAIN_IS("emailDomainIs"),
    IS_MEMBER_OF("isMemberOf");

    private final String name;
}