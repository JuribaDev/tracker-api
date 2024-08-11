package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.user.application.RoleService;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleServiceImp implements RoleService {
    private  final RoleRepository roleRepository;

    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findRoleByRoleName(String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(()-> new ResourceNotFoundException("Role not found"));
    }

    @Override
    @Transactional
    public void createRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
       var roleToUpdate = roleRepository.findById(role.getId()).orElseThrow();
       roleRepository.save(role);
    }

    @Override
    public void deleteRole(Role role) {

    }

    @Override
    public void createRoles(Set<Role> roles) {

    }
}
