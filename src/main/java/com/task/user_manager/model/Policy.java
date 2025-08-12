package com.task.user_manager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Entity
@Table(name = "policies")
@NoArgsConstructor
@AllArgsConstructor
public class Policy {

    @Id
    private String id;
    private String name;

    @ElementCollection
    @CollectionTable(name = "policy_conditions", joinColumns = @JoinColumn(name = "policy_id"))
    @MapKeyColumn(name = "condition_key")
    @Column(name = "condition_value")
    private Map<String, String> conditions;

}
