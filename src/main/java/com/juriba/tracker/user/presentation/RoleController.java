package com.juriba.tracker.user.presentation;

import com.juriba.tracker.user.application.imp.*;
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
    private final CreateRoleUseCaseImp createRoleUseCase;
    private final GetRoleUseCaseImp getRoleUseCase;
    private final UpdateRoleUseCaseImp updateRoleUseCase;
    private final DeleteRoleUseCaseImp deleteRoleUseCase;
    private final ListRolesUseCaseImp listRolesUseCase;

    public RoleController(CreateRoleUseCaseImp createRoleUseCase, GetRoleUseCaseImp getRoleUseCase, UpdateRoleUseCaseImp updateRoleUseCase, DeleteRoleUseCaseImp deleteRoleUseCase, ListRolesUseCaseImp listRolesUseCase) {
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
