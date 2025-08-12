package com.task.user_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.user_manager.dto.user.CreateUserRequest;
import com.task.user_manager.dto.user.UpdateUserRequest;
import com.task.user_manager.exception.UserNotFoundException;
import com.task.user_manager.model.User;
import com.task.user_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User getMockUser() {
        User user = new User();
        user.setName("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmailAddress("john.doe@example.com");
        user.setOrganizationUnit(List.of("IT"));
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setRegisteredOn(LocalDate.now());
        user.setPolicy(new HashSet<>());
        return user;
    }

    @Test
    void getAllUsers_shouldReturnUsers() throws Exception {
        User user = getMockUser();
        Page<User> page = new PageImpl<>(Collections.singletonList(user));
        Mockito.when(userService.findAll(0, 10)).thenReturn(page);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        Mockito.when(userService.find("john")).thenReturn(getMockUser());

        mockMvc.perform(get("/api/users/john"))
                .andExpect(status().isOk());
    }

    @Test
    void getUser_shouldReturnNotFound() throws Exception {
        Mockito.when(userService.find("notfound")).thenThrow(new UserNotFoundException("notfound"));
        mockMvc.perform(get("/api/users/notfound")).andExpect(status().isNotFound());
    }

    @Test
    void createUser_shouldReturnUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest();
        request.setName("jane");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmailAddress("jane.doe@example.com");
        request.setOrganizationUnit(List.of("HR"));
        request.setBirthDate(LocalDate.of(1992, 2, 2));

        Mockito.when(userService.create(any(CreateUserRequest.class))).thenReturn(getMockUser());
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void updateUser_shouldReturnUser() throws Exception {
        UpdateUserRequest request = new UpdateUserRequest();
        Mockito.when(userService.update(eq("john"), any(UpdateUserRequest.class))).thenReturn(getMockUser());
        mockMvc.perform(put("/api/users/john")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_shouldReturnOk() throws Exception {
        Mockito.doNothing().when(userService).delete("john");
        mockMvc.perform(delete("/api/users/john")).andExpect(status().isOk());
    }
}