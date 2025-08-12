package com.task.user_manager.dto.policy;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Map;

@Data
public class UpdatePolicyRequest {

    @Nullable
    private String name;
    @Nullable
    private Map<String, String> conditions;
}
