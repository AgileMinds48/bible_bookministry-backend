/*
package com.evbooksministry.bibleandbookministry.config;

import com.evbooksministry.bibleandbookministry.models.Role;
import com.evbooksministry.bibleandbookministry.repositories.RoleRepository;
import com.evbooksministry.bibleandbookministry.services.CategoryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final RoleRepository roleRepository;
    private final CategoryService categoryService;
    
    public DataInitializer(RoleRepository roleRepository, CategoryService categoryService) {
        this.roleRepository = roleRepository;
        this.categoryService = categoryService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        initializeDefaultRoles();
        categoryService.seedDefaults();
    }
    
    private void initializeDefaultRoles() {
        // Create ADMIN role if it doesn't exist
        if (!roleRepository.existsByRoleCode("ADMIN")) {
            Role adminRole = Role.builder()
                    .roleName("Administrator")
                    .roleCode("ADMIN")
                    .description("Full system administrator with all permissions")
                    .isActive(true)
                    .build();
            roleRepository.save(adminRole);
            System.out.println("Created ADMIN role");
        }
        
        // Create CUSTOMER role if it doesn't exist
        if (!roleRepository.existsByRoleCode("CUSTOMER")) {
            Role customerRole = Role.builder()
                    .roleName("Customer")
                    .roleCode("CUSTOMER")
                    .description("Regular customer with limited permissions")
                    .isActive(true)
                    .build();
            roleRepository.save(customerRole);
            System.out.println("Created CUSTOMER role");
        }
    }
}
*/
