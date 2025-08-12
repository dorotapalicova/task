package com.task.user_manager.dto.policy;

import com.task.user_manager.dto.PaginatedResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public class PoliciesResponse extends PaginatedResponse<PolicyResponse> {

    public PoliciesResponse(HttpServletRequest request, Page<PolicyResponse> page) {
        super(request, page);
    }
}
