/*
    File: InventoryViewModel.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: ViewModel for managing inventory data and SMS badge updates.
             Responsibilities:
             - Provide a clean interface between UI and InventoryDatabase.
             - Perform CRUD operations on inventory items.
             - Filter items by All, Low Stock, or Out of Stock.
             - Simulate SMS badge updates for low-stock items.
             - Retrieve and update SMS notification preferences for the current user.
    Notes:
    - Extends AndroidViewModel to retain application context across configuration changes.
    - Ensures UI components do not directly manipulate the database.
    - checkAndUpdateBadge() only triggers simulated SMS for items with 1–5 quantity on add/update.
    - SMS messages are not generated for existing low-stock items loaded from the database.

    SMS Functionality:
    - When an item falls below the threshold of 5, a "message" is generated if the user has
    the option saved for their account in the database. After such an edit or add action,
    a message count bubble will appear over the SMS icon in the upper right hand corner.
    - This supports the user case:
      - User edits/adds item with count > 1, and <= 5.
      - The message badge is incremented.
*/
package com.example.inventoryapp;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.ArrayList;
import java.util.List;

public class InventoryViewModel extends AndroidViewModel {

    private final InventoryDatabase db;

    public InventoryViewModel(@NonNull Application application) {
        super(application);
        db = InventoryDatabase.getInstance(application);
    }

    // ------------------------
    // Database initialization
    // ------------------------
    public void initDefaultItems() {
        db.initDefaultItems();
    }

    // ------------------------
    // CRUD access
    // ------------------------
    public List<Item> getItems() {
        return db.getItems();
    }

    // This implements the user inventory filter
    public List<Item> getItemsFiltered(String filter) {
        List<Item> allItems = db.getItems();
        if ("ALL".equals(filter)) return allItems;

        List<Item> filtered = new ArrayList<>();
        for (Item item : allItems) {
            if ("LOW_STOCK".equals(filter) && item.getQuantity() > 0 && item.getQuantity() <= 5) {
                filtered.add(item);
            } else if ("OUT_OF_STOCK".equals(filter) && item.getQuantity() == 0) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    // Add an item
    public void addItem(Item item) {
        db.addItem(item);
        checkAndUpdateBadge(item);
    }

    // Update an item
    public void updateItem(Item item) {
        db.updateItem(item);
        checkAndUpdateBadge(item);
    }

    // Delete an item
    public void deleteItem(int id) {
        db.deleteItem(id);
    }

    // ------------------------
    // Badge update (simulated SMS)
    // ------------------------
    private void checkAndUpdateBadge(Item item) {
        // Only increment badge if the user has SMS notifications enabled
        if (!getSmsEnabled()) return;

        // Increment SMS badge if quantity is zero or low stock
        if (item.getQuantity() == 0 || item.getQuantity() <= 5) {
            int count = AppSession.getSmsBadgeCount();
            AppSession.setSmsBadgeCount(count + 1);
        }
    }

    // ------------------------
    // SMS toggle (just keeps track in DB)
    // ------------------------
    public boolean getSmsEnabled() {
        String currentUser = AppSession.getCurrentUser();
        return currentUser != null && db.getSmsEnabled(currentUser);
    }

    public void setSmsEnabled(boolean enabled) {
        String currentUser = AppSession.getCurrentUser();
        if (currentUser != null) {
            db.setSmsEnabled(currentUser, enabled);
        }
    }
}
