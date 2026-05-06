package com.example.lab01.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "jpa_orders")
public class JpaOrderEntity {

    @Id
    private Long id;

    @Column(name = "owner", nullable = false, length = 64)
    private String owner;

    @Column(name = "item_name", nullable = false, length = 128)
    private String itemName;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    protected JpaOrderEntity() {
    }

    public JpaOrderEntity(Long id, String owner, String itemName, String status) {
        this.id = id;
        this.owner = owner;
        this.itemName = itemName;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getItemName() {
        return itemName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
