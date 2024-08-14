package com.juriba.tracker.user.application;

import com.juriba.tracker.user.presentation.dto.RoleResponse;

import java.util.List;

public interface ListRolesUseCase {
    List<RoleResponse> execute();
}
