package com.juriba.tracker.user.application;

import com.juriba.tracker.user.domain.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    Role findRoleByRoleName(String roleName);
    void createRole(Role role);
    void updateRole(Role role);
    void deleteRole(Role role);
    void createRoles(Set<Role> roles);

}
