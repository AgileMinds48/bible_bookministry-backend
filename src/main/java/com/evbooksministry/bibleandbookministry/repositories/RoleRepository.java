package com.evbooksministry.bibleandbookministry.repositories;

import com.evbooksministry.bibleandbookministry.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    Optional<Role> findByRoleCode(String roleCode);
    
    Optional<Role> findByRoleName(String roleName);
    
    List<Role> findByIsActiveTrue();
    
    @Query("SELECT r FROM Role r WHERE r.roleCode IN :roleCodes")
    List<Role> findByRoleCodes(@Param("roleCodes") List<String> roleCodes);
    
    boolean existsByRoleCode(String roleCode);
    
    boolean existsByRoleName(String roleName);
}
