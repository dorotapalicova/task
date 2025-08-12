package com.task.user_manager.controller;

import com.task.user_manager.dto.policy.CreatePolicyRequest;
import com.task.user_manager.dto.policy.PoliciesResponse;
import com.task.user_manager.dto.policy.PolicyResponse;
import com.task.user_manager.dto.policy.UpdatePolicyRequest;
import com.task.user_manager.exception.PolicyNotFoundException;
import com.task.user_manager.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@Tag(name = "Policies")
@RequiredArgsConstructor
@RequestMapping(value = "/api/policies")
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
    })
    @Operation(
            summary = "Request all policies",
            description = "Retrieve pageable list with detailed information about all policies."
    )
    public PoliciesResponse getAllPolicies(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") @Max(100) Integer length) {
        return new PoliciesResponse(request, policyService.findAll(page, length).map(PolicyResponse::new));
    }

    @GetMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Policy was not found on server."),
    })
    @Operation(
            summary = "Request policy information",
            description = "Retrieve detailed information about a policy by their name."
    )
    PolicyResponse getPolicy(@PathVariable @Valid String name) throws PolicyNotFoundException {
        return new PolicyResponse(this.policyService.find(name));
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Policy condition type is not supported."),
    })
    @Operation(
            summary = "Create new policy",
            description = "Create new policy."
    )
    public PolicyResponse createPolicy(@Valid @RequestBody CreatePolicyRequest request) throws IllegalArgumentException {
        return new PolicyResponse(this.policyService.create(request));
    }

    @PutMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Policy was not found on server.", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
            @ApiResponse(responseCode = "400", description = "Policy condition type is not supported."),
    })
    @Operation(
            summary = "Update policy information",
            description = "Update information of a policy by their name."
    )
    public PolicyResponse updatePolicy(
            @PathVariable String name,
            @Valid @RequestBody UpdatePolicyRequest request
    ) throws PolicyNotFoundException, IllegalArgumentException {
        return new PolicyResponse(this.policyService.update(name, request));
    }

    @DeleteMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Policy was not found on server."),
    })
    @Operation(
            summary = "Delete policy profile",
            description = "Delete a policy by their name."
    )
    public void deletePolicy(@PathVariable String name) throws PolicyNotFoundException {
        this.policyService.delete(name);
    }
}
