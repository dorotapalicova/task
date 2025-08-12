package com.task.user_manager.controller;

import com.task.user_manager.dto.user.CreateUserRequest;
import com.task.user_manager.dto.user.UpdateUserRequest;
import com.task.user_manager.dto.user.UserResponse;
import com.task.user_manager.dto.user.UsersResponse;
import com.task.user_manager.exception.UserNotFoundException;
import com.task.user_manager.service.UserService;
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
@Tag(name = "Users")
@RequiredArgsConstructor
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
    })
    @Operation(
            summary = "Request all users",
            description = "Retrieve pageable list with detailed information about all users."
    )
    public UsersResponse getAllUsers(
            HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") @Max(100) Integer length) {
        return new UsersResponse(request, userService.findAll(page, length).map(UserResponse::new));
    }

    @GetMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "User was not found on server", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
    })
    @Operation(
            summary = "Request user profile information",
            description = "Retrieve detailed information about a user by their username."
    )
    UserResponse getUser(@PathVariable @Valid String name) throws UserNotFoundException {
        return new UserResponse(this.userService.find(name));
    }

    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
    })
    @Operation(
            summary = "Create new user",
            description = "Create new user."
    )
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return new UserResponse(this.userService.create(request));
    }

    @PutMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User was not found on server", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
    })
    @Operation(
            summary = "Update user profile information",
            description = "Update information of a user by their username."
    )
    public UserResponse updateUser(
            @PathVariable String name,
            @Valid @RequestBody UpdateUserRequest request
    ) throws UserNotFoundException {
        return new UserResponse(this.userService.update(name, request));
    }

    @DeleteMapping("/{name}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User was not found on server", content = @Content(
                    schema = @Schema(implementation = ErrorResponse.class)
            )),
    })
    @Operation(
            summary = "Delete user profile",
            description = "Delete a user by their username."
    )
    public void deleteUser(@PathVariable String name) throws UserNotFoundException {
        this.userService.delete(name);
    }
}
