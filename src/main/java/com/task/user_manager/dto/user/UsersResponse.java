package com.task.user_manager.dto.user;

import com.task.user_manager.dto.PaginatedResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public class UsersResponse extends PaginatedResponse<UserResponse> {

    public UsersResponse(HttpServletRequest request, Page<UserResponse> page) {
        super(request, page);
    }
}
