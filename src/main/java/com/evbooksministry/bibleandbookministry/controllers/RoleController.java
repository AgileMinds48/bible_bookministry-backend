package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.JWTService;
import com.evbooksministry.bibleandbookministry.dtos.CreateRoleRequest;
import com.evbooksministry.bibleandbookministry.dtos.RoleDTO;
import com.evbooksministry.bibleandbookministry.services.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    
    private final RoleService roleService;
    private final HttpServletRequest httpServletRequest;
    private final JWTService jwtService;
    
    public RoleController(RoleService roleService, HttpServletRequest httpServletRequest, JWTService jwtService) {
        this.roleService = roleService;
        this.httpServletRequest = httpServletRequest;
        this.jwtService = jwtService;
    }
    
    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody CreateRoleRequest request) {
        UUID userId = jwtService.getAdminId(httpServletRequest);
        System.out.println("admin Id " + userId);
        RoleDTO createdRole = roleService.createRole(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }
    
    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable UUID roleId) {
        RoleDTO role = roleService.getRoleById(roleId);
        return ResponseEntity.ok(role);
    }
    
    @GetMapping("/code/{roleCode}")
    public ResponseEntity<RoleDTO> getRoleByCode(@PathVariable String roleCode) {
        RoleDTO role = roleService.getRoleByCode(roleCode);
        return ResponseEntity.ok(role);
    }
    
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles(@RequestParam(defaultValue = "false") boolean activeOnly) {
        List<RoleDTO> roles = activeOnly ? 
            roleService.getAllActiveRoles() : 
            roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }
    
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable UUID roleId, @RequestBody CreateRoleRequest request) {
        RoleDTO updatedRole = roleService.updateRole(roleId, request);
        return ResponseEntity.ok(updatedRole);
    }
    
    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable UUID roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{roleId}/deactivate")
    public ResponseEntity<Void> deactivateRole(@PathVariable UUID roleId) {
        roleService.deactivateRole(roleId);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{roleId}/activate")
    public ResponseEntity<Void> activateRole(@PathVariable UUID roleId) {
        roleService.activateRole(roleId);
        return ResponseEntity.ok().build();
    }
}
