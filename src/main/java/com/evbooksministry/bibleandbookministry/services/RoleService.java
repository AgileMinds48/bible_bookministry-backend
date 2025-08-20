package com.evbooksministry.bibleandbookministry.services;

import com.evbooksministry.bibleandbookministry.dtos.CreateRoleRequest;
import com.evbooksministry.bibleandbookministry.dtos.RoleDTO;
import com.evbooksministry.bibleandbookministry.exceptions.RoleAlreadyExistsException;
import com.evbooksministry.bibleandbookministry.exceptions.RoleNotFoundException;
import com.evbooksministry.bibleandbookministry.models.Role;
import com.evbooksministry.bibleandbookministry.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {
    
    private final RoleRepository roleRepository;
    
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    
    public RoleDTO createRole(CreateRoleRequest request) {
        // Check if role code already exists
        if (roleRepository.existsByRoleCode(request.roleCode())) {
            throw new RoleAlreadyExistsException("Role with code " + request.roleCode() + " already exists");
        }
        
        // Check if role name already exists
        if (roleRepository.existsByRoleName(request.roleName())) {
            throw new RoleAlreadyExistsException("Role with name " + request.roleName() + " already exists");
        }
        
        Role role = Role.builder()
                .roleName(request.roleName())
                .roleCode(request.roleCode())
                .description(request.description())
                .isActive(true)
                .build();
        
        Role savedRole = roleRepository.save(role);
        return convertToDTO(savedRole);
    }
    
    public RoleDTO getRoleById(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
        return convertToDTO(role);
    }
    
    public RoleDTO getRoleByCode(String roleCode) {
        Role role = roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with code: " + roleCode));
        return convertToDTO(role);
    }
    
    public List<RoleDTO> getAllActiveRoles() {
        return roleRepository.findByIsActiveTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public RoleDTO updateRole(UUID roleId, CreateRoleRequest request) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
        
        // Check if new role code conflicts with existing roles
        if (!role.getRoleCode().equals(request.roleCode()) && 
            roleRepository.existsByRoleCode(request.roleCode())) {
            throw new RoleAlreadyExistsException("Role with code " + request.roleCode() + " already exists");
        }
        
        // Check if new role name conflicts with existing roles
        if (!role.getRoleName().equals(request.roleName()) && 
            roleRepository.existsByRoleName(request.roleName())) {
            throw new RoleAlreadyExistsException("Role with name " + request.roleName() + " already exists");
        }
        
        role.setRoleName(request.roleName());
        role.setRoleCode(request.roleCode());
        role.setDescription(request.description());
        
        Role updatedRole = roleRepository.save(role);
        return convertToDTO(updatedRole);
    }
    
    public void deleteRole(UUID roleId) {
        if (!roleRepository.existsById(roleId)) {
            throw new RoleNotFoundException("Role not found with id: " + roleId);
        }
        roleRepository.deleteById(roleId);
    }
    
    public void deactivateRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
        role.setActive(false);
        roleRepository.save(role);
    }
    
    public void activateRole(UUID roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
        role.setActive(true);
        roleRepository.save(role);
    }
    
    // Helper method to get Role entity by code
    public Role getRoleEntityByCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with code: " + roleCode));
    }
    
    private RoleDTO convertToDTO(Role role) {
        return new RoleDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getRoleCode(),
                role.getDescription(),
                role.isActive()
        );
    }
}
