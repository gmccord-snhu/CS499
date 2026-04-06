/*
    File: SmsSettingsActivity.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Activity for managing SMS notification settings in InventoryApp.
             Responsibilities:
             - Display a toggle to enable or disable SMS notifications
             - Show permission status or warning when SMS is disabled
             - Persist toggle state using InventoryViewModel and database
    Notes:
    - Uses Material3 components: SwitchMaterial, MaterialToolbar
    - Animations implemented with AutoTransition and TransitionManager
    - Bottom navigation maintains selection state for Settings tab

    The SMS functionality is handled through this activity, which acts as a
    permission/notification settings screen. User preferences are saved via
    the InventoryViewModel to the database, keeping the UI separate from
    data storage logic.
*/
package com.example.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SmsSettingsActivity extends AppCompatActivity {

    private SwitchMaterial smsToggle;
    private TextView permissionStatus;
    private ConstraintLayout smsContainer;
    private InventoryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_settings);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        // Bottom Navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_inventory) {
                startActivity(new Intent(this, InventoryListActivity.class));
                return true;
            }
            return id == R.id.nav_settings;
        });

        // Views
        smsToggle = findViewById(R.id.sms_toggle_switch);
        permissionStatus = findViewById(R.id.permission_status_text);
        smsContainer = findViewById(R.id.sms_setting_row);

        // ViewModel
        viewModel = new InventoryViewModel(getApplication());

        // Load saved state from DB
        boolean smsEnabled = viewModel.getSmsEnabled();
        smsToggle.setChecked(smsEnabled);
        animatePermissionStatus(!smsEnabled);

        // Toggle listener
        smsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setSmsEnabled(isChecked);
            animatePermissionStatus(!isChecked);
        });
    }

    // Animate show/hide of the permission warning with slow fade in/out
    private void animatePermissionStatus(boolean show) {
        AutoTransition transition = new AutoTransition();
        transition.setDuration(200);
        TransitionManager.beginDelayedTransition(smsContainer, transition);
        permissionStatus.setVisibility(show ? TextView.VISIBLE : TextView.GONE);
    }
}
