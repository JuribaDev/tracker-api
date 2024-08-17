package com.juriba.tracker.user.application;

import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;

public interface DeleteRoleUseCase {
    CommonSuccessResponse execute(String id);
}
