/*
    File: Item.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Model class representing an inventory item.
             Contains properties:
             - id: unique identifier for the item
             - name: descriptive name of the item
             - quantity: current stock quantity of the item
    Notes:
    - Provides default and parameterized constructors
    - Includes standard getters and setters for all fields
    - Used throughout InventoryViewModel and InventoryListActivity for data operations
*/
package com.example.inventoryapp;

public class Item {
    private int id;
    private String name;
    private int quantity;

    public Item() {}

    public Item(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // getters + setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}