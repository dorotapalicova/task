package com.task.user_manager.dto.user;

import com.task.user_manager.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
public class UserResponse {
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<String> organizationUnit;
    private LocalDate birthDate;
    private LocalDate registeredOn;
    private Set<String> policy;

    public UserResponse(User user) {
        this.name = user.getName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.emailAddress = user.getEmailAddress();
        this.organizationUnit = user.getOrganizationUnit();
        this.birthDate = user.getBirthDate();
        this.registeredOn = user.getRegisteredOn();
        this.policy = user.getPolicy();
    }
}
