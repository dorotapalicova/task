package com.task.user_manager.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateUserRequest {

    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String emailAddress;
    @Nullable
    private List<String> organizationUnit;
    @PastOrPresent
    private LocalDate birthDate;
}
