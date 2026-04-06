/*
    File: InventoryListActivity.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Activity to display and manage the inventory list.
             Features include:
             - RecyclerView showing inventory items
             - Filter buttons (All, Low Stock, Out of Stock)
             - Add/Edit/Delete inventory items via MaterialAlertDialogBuilder
             - SMS menu icon with dynamic badge (shown only if enabled by user)
             - Bottom navigation to switch between Inventory and SMS Settings
    Notes:
    - Uses InventoryViewModel for data management
    - SMS badge count is simulated using AppSession
    - SMS menu icon visibility respects user's SMS preference
    - Dialog layout is unified for both Add and Edit modes
    - BottomNavigationView updates selected item and launches settings activity
*/
package com.example.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Objects;

public class InventoryListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private InventoryViewModel viewModel;
    private TextView smsBadge;

    private MaterialButton buttonAll, buttonLowStock, buttonOutOfStock, buttonAddItem;
    private String currentFilter = "ALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        setSupportActionBar(findViewById(R.id.toolbar));

        recyclerView = findViewById(R.id.inventory_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        buttonAll = findViewById(R.id.button_all);
        buttonLowStock = findViewById(R.id.button_low_stock);
        buttonOutOfStock = findViewById(R.id.button_out_of_stock);
        buttonAddItem = findViewById(R.id.button_add_item);

        viewModel = new InventoryViewModel(getApplication());
        viewModel.initDefaultItems();

        // Filter buttons click listeners
        buttonAll.setOnClickListener(v -> {
            currentFilter = "ALL";
            updateUI();
        });
        buttonLowStock.setOnClickListener(v -> {
            currentFilter = "LOW_STOCK";
            updateUI();
        });
        buttonOutOfStock.setOnClickListener(v -> {
            currentFilter = "OUT_OF_STOCK";
            updateUI();
        });

        // Add/Edit item
        buttonAddItem.setOnClickListener(v -> showEditItemDialog(null));

        // Bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_inventory);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_inventory) return true;
            if (itemId == R.id.nav_reports) {
                startActivity(new Intent(this, ReportsActivity.class));
                return true;
            }
            if (itemId == R.id.nav_settings) {
                startActivity(new Intent(this, SmsSettingsActivity.class));
                return true;
            }
            return false;
        });

        updateUI(); // initial load
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_sms_menu, menu);
        MenuItem smsItem = menu.findItem(R.id.action_sms);
        if (smsItem != null) {
            // Show SMS icon only if user has enabled it
            smsItem.setVisible(viewModel.getSmsEnabled());

            View actionView = smsItem.getActionView();
            if (actionView != null) {
                smsBadge = actionView.findViewById(R.id.sms_badge);
                updateSmsBadge();
                actionView.setOnClickListener(v -> onOptionsItemSelected(smsItem));
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sms) {
            startActivity(new Intent(this, SmsSettingsActivity.class));

            // Leave SMS counts intact when accessing this screen
            // (was resetting to zero, removed that code)

            updateSmsBadge();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateSmsBadge() {
        int count = AppSession.getSmsBadgeCount();
        if (smsBadge != null) {
            smsBadge.setText(String.valueOf(count));
            smsBadge.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
        }
    }

    private void updateUI() {
        if (viewModel == null) return;

        List<Item> items = viewModel.getItemsFiltered(currentFilter);
        adapter = new ItemAdapter(items, viewModel, this::showEditItemDialog, this::updateUI);
        recyclerView.setAdapter(adapter);

        buttonAll.setSelected(currentFilter.equals("ALL"));
        buttonLowStock.setSelected(currentFilter.equals("LOW_STOCK"));
        buttonOutOfStock.setSelected(currentFilter.equals("OUT_OF_STOCK"));
    }

    private void showEditItemDialog(Item itemToEdit) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_item, null);
        TextInputEditText editName = dialogView.findViewById(R.id.edit_item_name);
        TextInputEditText editQty = dialogView.findViewById(R.id.edit_item_quantity);
        boolean isEditMode = itemToEdit != null;

        if (isEditMode) {
            editName.setText(itemToEdit.getName());
            editQty.setText(String.valueOf(itemToEdit.getQuantity()));
        }

        // Dynamic dialog builder for edit/remove options
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(isEditMode ? "Edit Item" : "Add New Item")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = Objects.requireNonNull(editName.getText()).toString().trim();
                    String qtyText = Objects.requireNonNull(editQty.getText()).toString().trim();

                    // Validate user attempts to modify
                    if (name.isEmpty() || qtyText.isEmpty()) {
                        Toast.makeText(this, "Name and Quantity cannot be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int quantity;
                    try {
                        quantity = Integer.parseInt(qtyText);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Quantity must be a number.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate user attempts to modify
                    if (quantity < 0) {
                        Toast.makeText(this, "Quantity must be >= 0.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (viewModel == null) return;

                    if (isEditMode) {
                        if (itemToEdit != null) {
                            itemToEdit.setName(name);
                            itemToEdit.setQuantity(quantity);
                            viewModel.updateItem(itemToEdit);
                        }
                    } else {
                        viewModel.addItem(new Item(name, quantity));
                    }

                    // Simulate "new SMS" badge update
                    updateSmsBadge();

                    updateUI();
                })
                .setNegativeButton("Cancel", null);

        if (isEditMode) {
            builder.setNeutralButton("Remove", (dialog, which) -> {
                if (itemToEdit != null && viewModel != null) {
                    viewModel.deleteItem(itemToEdit.getId());
                    updateUI();
                }
            });
        }

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh SMS icon visibility in case user toggled it in settings
        invalidateOptionsMenu();
    }
}
