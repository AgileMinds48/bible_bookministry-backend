package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
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
    @JoinColumn(name = "user_Id")
    private Users createdBy;


    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
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
