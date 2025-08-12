package com.task.user_manager.dto.policy;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "Map of condition keys and their values. Allowed keys: youngerThan, emailDomainIs, isMemberOf")
    private Map<String, String> conditions;
}
