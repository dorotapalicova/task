package com.task.user_manager.dto.policy;

import com.task.user_manager.model.Policy;
import lombok.Data;

import java.util.Map;

@Data
public class PolicyResponse {
    private String id;
    private String name;
    private Map<String, String> conditions;

    public PolicyResponse(Policy policy) {
        this.id = policy.getId();
        this.name = policy.getName();
        this.conditions = policy.getConditions();
    }
}
