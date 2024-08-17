package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.application.GetRoleUseCase;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import com.juriba.tracker.user.presentation.mapper.RoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
public class GetRoleUseCaseImp implements GetRoleUseCase {
    private final RoleRepository roleRepository;

    public GetRoleUseCaseImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse execute(String  id) {
        log.info("Getting role with id: {}", id);
        return roleRepository.findById(id).map(RoleMapper::toRoleResponse)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }
}