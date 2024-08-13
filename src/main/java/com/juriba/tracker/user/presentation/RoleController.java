package com.juriba.tracker.user.presentation;

import com.juriba.tracker.user.application.*;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@Tag(name = "Roles Endpoints")
public class RoleController {
    private final CreateRoleUseCase createRoleUseCase;
    private final GetRoleUseCase getRoleUseCase;
    private final UpdateRoleUseCase updateRoleUseCase;
    private final DeleteRoleUseCase deleteRoleUseCase;
    private final ListRolesUseCase listRolesUseCase;

    public RoleController(CreateRoleUseCase createRoleUseCase, GetRoleUseCase getRoleUseCase, UpdateRoleUseCase updateRoleUseCase, DeleteRoleUseCase deleteRoleUseCase, ListRolesUseCase listRolesUseCase) {
        this.createRoleUseCase = createRoleUseCase;
        this.getRoleUseCase = getRoleUseCase;
        this.updateRoleUseCase = updateRoleUseCase;
        this.deleteRoleUseCase = deleteRoleUseCase;
        this.listRolesUseCase = listRolesUseCase;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody RoleRequest roleName) {
        createRoleUseCase.execute(roleName);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable String id) {
        return ResponseEntity.ok(getRoleUseCase.execute(id));
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRole(@Validated @PathVariable String id, @RequestBody RoleRequest role) {
        updateRoleUseCase.execute(id, role);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        deleteRoleUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RoleResponse>> listRoles() {
        return ResponseEntity.ok(listRolesUseCase.execute());
    }
}
