package com.evbooksministry.bibleandbookministry.controllers;

import com.evbooksministry.bibleandbookministry.config.RoleAuthorizationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    
    private final RoleAuthorizationUtils roleUtils;
    
    public TestController(RoleAuthorizationUtils roleUtils) {
        this.roleUtils = roleUtils;
    }
    
    @GetMapping("/admin-only")
    public ResponseEntity<Map<String, Object>> adminOnly() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is admin only endpoint");
        response.put("currentUser", roleUtils.getCurrentUsername());
        response.put("currentRole", roleUtils.getCurrentUserRole());
        response.put("isAdmin", roleUtils.isAdmin());
        response.put("isCustomer", roleUtils.isCustomer());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/customer-only")
    public ResponseEntity<Map<String, Object>> customerOnly() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is customer only endpoint");
        response.put("currentUser", roleUtils.getCurrentUsername());
        response.put("currentRole", roleUtils.getCurrentUserRole());
        response.put("isAdmin", roleUtils.isAdmin());
        response.put("isCustomer", roleUtils.isCustomer());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/shared")
    public ResponseEntity<Map<String, Object>> shared() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "This is shared endpoint");
        response.put("currentUser", roleUtils.getCurrentUsername());
        response.put("currentRole", roleUtils.getCurrentUserRole());
        response.put("isAdmin", roleUtils.isAdmin());
        response.put("isCustomer", roleUtils.isCustomer());
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/auth-info")
    public ResponseEntity<Map<String, Object>> authInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        response.put("authenticated", authentication != null && authentication.isAuthenticated());
        response.put("username", authentication != null ? authentication.getName() : "none");
        response.put("authorities", authentication != null ? authentication.getAuthorities() : "none");
        response.put("principal", authentication != null ? authentication.getPrincipal().getClass().getSimpleName() : "none");
        return ResponseEntity.ok(response);
    }
}
