package com.task.user_manager.dto.policy;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.util.Map;

@Data
public class UpdatePolicyRequest {

    @Nullable
    private String name;
    @Nullable
    @Schema(description = "Map of condition keys and their values. Allowed keys: youngerThan, emailDomainIs, isMemberOf")
    private Map<String, String> conditions;
}
