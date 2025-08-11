package com.task.user_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String name;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private List<String> organizationUnit;
    private LocalDate birthDate;
    private LocalDate registeredOn;
    private Set<String> policy = new HashSet<>();
}
