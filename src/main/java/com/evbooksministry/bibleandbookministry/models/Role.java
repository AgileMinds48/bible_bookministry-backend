package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Builder;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;


@Builder
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID roleId;

    @Column(nullable = false, unique = true)
    private String roleName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false)
    @JsonBackReference
    private Users createdBy;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    public Role() {
    }

    public Role(UUID roleId,
                String roleName,
                Users createdBy,
                Timestamp createdAt,
                Timestamp updatedAt,
                DeleteYn deleteYn) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deleteYn = deleteYn;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = Timestamp.from(Instant.now());
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

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
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
}
