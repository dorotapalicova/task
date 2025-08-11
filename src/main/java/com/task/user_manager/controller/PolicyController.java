package com.task.user_manager.controller;

import com.task.user_manager.dto.user.UsersResponse;
import com.task.user_manager.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @Operation(
            summary = "Request all policies",
            description = "Retrieve pageable list with detailed information about all users."
    )
    public UsersResponse getAllPolicies(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") @Max(100) Integer length) {
        return new PoliciesResponse(request, policyService.findAll(page, length).map(PolicyResponse::new));
    }

    @GetMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @Operation(
            summary = "Request policy information",
            description = "Retrieve detailed information about a policy by their name."
    )
    PolicyResponse getPolicy(@PathVariable @Valid String name) {
        return new PolicyResponse(this.policyService.find(name));
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @Operation(
            summary = "Create new user",
            description = "Create new user."
    )
    public PolicyResponse createUser(@Valid @RequestBody CreatePolicyRequest request) {
        return new PolicyResponse(this.policyService.create(request));
    }

    @PutMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @Operation(
            summary = "Update policy information",
            description = "Update information of a policy by their name."
    )
    public PolicyResponse updateUser(
            @PathVariable String name,
            @Valid @RequestBody UpdatePolicyRequest request
    ) {
        return new PolicyResponse(this.policyService.update(name, request));
    }

    @DeleteMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    @Operation(
            summary = "Delete policy profile",
            description = "Delete a policy by their name."
    )
    public void deleteUser(@PathVariable String name) {
        this.policyService.delete(name);
    }
}
