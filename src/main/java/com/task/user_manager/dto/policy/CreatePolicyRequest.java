package com.task.user_manager.dto.policy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class CreatePolicyRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
    @NotNull
    private Map<String, String> conditions;
}
