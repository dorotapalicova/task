package com.task.user_manager.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateUserRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String emailAddress;
    private List<String> organizationUnit;
    @NotNull
    @PastOrPresent
    private LocalDate birthDate;
}
