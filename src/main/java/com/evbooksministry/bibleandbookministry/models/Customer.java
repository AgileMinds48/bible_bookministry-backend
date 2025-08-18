package com.evbooksministry.bibleandbookministry.models;

import com.evbooksministry.bibleandbookministry.enums.DeleteYn;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Entity
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    private DeleteYn deleteYn;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<CustomerOrders> orders;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId", nullable = false, unique = true)
    @JsonBackReference
    private Users user;

    public Customer(UUID customerId, DeleteYn deleteYn, Set<CustomerOrders> orders, Users user) {
        this.customerId = customerId;
        this.deleteYn = deleteYn;
        this.orders = orders;
        this.user = user;
    }

    @PrePersist
    protected void onCreate(){
        this.deleteYn = DeleteYn.N;
    }

/*    @PreUpdate
    protected void onUpdate(){
    }*/

    public Customer() {
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public DeleteYn getDeleteYn() {
        return deleteYn;
    }

    public void setDeleteYn(DeleteYn deleteYn) {
        this.deleteYn = deleteYn;
    }
}
