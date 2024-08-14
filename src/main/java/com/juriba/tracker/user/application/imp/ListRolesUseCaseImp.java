package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.application.ListRolesUseCase;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import com.juriba.tracker.user.presentation.mapper.RoleMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
public class ListRolesUseCaseImp implements ListRolesUseCase {
    private final RoleRepository roleRepository;

    public ListRolesUseCaseImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleResponse> execute() {
        return roleRepository.findAll().stream().map(RoleMapper::toRoleResponse).collect(Collectors.toList());
    }
}
