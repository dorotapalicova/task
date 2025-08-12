package com.task.user_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.user_manager.dto.policy.CreatePolicyRequest;
import com.task.user_manager.dto.policy.UpdatePolicyRequest;
import com.task.user_manager.exception.PolicyNotFoundException;
import com.task.user_manager.model.Policy;
import com.task.user_manager.service.PolicyService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Policy getMockPolicy() {
        Map<String, String> conditions = new HashMap<>();
        conditions.put("type", "value");
        return new Policy("id1", "policy1", conditions);
    }

    @Test
    void getAllPolicies_shouldReturnPolicies() throws Exception {
        Policy policy = getMockPolicy();
        Page<Policy> page = new PageImpl<>(Collections.singletonList(policy));
        Mockito.when(policyService.findAll(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/policies"))
                .andExpect(status().isOk());
    }

    @Test
    void getPolicy_shouldReturnPolicy() throws Exception {
        Mockito.when(policyService.find("policy1")).thenReturn(getMockPolicy());

        mockMvc.perform(get("/api/policies/policy1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("policy1"));
    }

    @Test
    void getPolicy_shouldReturnNotFound() throws Exception {
        Mockito.when(policyService.find("notfound")).thenThrow(new PolicyNotFoundException("notfound"));

        mockMvc.perform(get("/api/policies/notfound"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPolicy_shouldReturnPolicy() throws Exception {
        CreatePolicyRequest request = new CreatePolicyRequest();
        request.setId("id1");
        request.setName("policy1");
        request.setConditions(Map.of("youngerThan", "value"));

        Mockito.when(policyService.create(any(CreatePolicyRequest.class))).thenReturn(getMockPolicy());

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("policy1"));
    }

    @Test
    void updatePolicy_shouldReturnPolicy() throws Exception {
        UpdatePolicyRequest request = new UpdatePolicyRequest();
        Mockito.when(policyService.update(eq("policy1"), any(UpdatePolicyRequest.class))).thenReturn(getMockPolicy());

        mockMvc.perform(put("/api/policies/policy1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("policy1"));
    }

    @Test
    void deletePolicy_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(policyService).delete("policy1");

        mockMvc.perform(delete("/api/policies/policy1"))
                .andExpect(status().isOk());
    }
}