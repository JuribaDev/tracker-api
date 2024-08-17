package com.juriba.tracker.user.application;

import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;

public interface AssignRoleToUserUseCase {
    CommonSuccessResponse execute(String userId, String roleId);
}
