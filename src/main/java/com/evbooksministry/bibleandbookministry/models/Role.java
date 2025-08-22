package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;
    
    @Column(nullable = false, unique = true)
    private String roleName;
    
    @Column(nullable = false, unique = true)
    private String roleCode; // e.g., "ADMIN", "CUSTOMER"
    
    @Column(length = 500)
    private String description;
    
    @Builder.Default
    private boolean isActive = true;
    
    @CreationTimestamp
    private Timestamp createdAt;
    
    @UpdateTimestamp
    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @OneToOne
    @JoinColumn(name = "created_by")
    private Users createdBy;


    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    /*    @ManyToMany(mappedBy = "roles")
    @Builder.Default
    private Set<Users> users = new HashSet<>();*/
    
/*    // Helper methods
    public void addUser(Users user) {
        this.users.add(user);
        user.getRoles().add(this);
    }
    
    public void removeUser(Users user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }*/
}
