package com.example.lab01.model;

public class OrderRecord {

    private final int id;
    private final String owner;
    private final String itemName;
    private final int amount;

    public OrderRecord(int id, String owner, String itemName, int amount) {
        this.id = id;
        this.owner = owner;
        this.itemName = itemName;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getItemName() {
        return itemName;
    }

    public int getAmount() {
        return amount;
    }
}
