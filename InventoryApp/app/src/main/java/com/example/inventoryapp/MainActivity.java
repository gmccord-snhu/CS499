/*
    File: MainActivity.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Main login screen for InventoryApp.
             Responsibilities:
             - Allow users to sign in with username and password
             - Option to remember username using SharedPreferences
             - Validate credentials against SQLite database
             - Provide "Create New Account" functionality
             - Navigate to InventoryListActivity after successful login
    Notes:
    - Uses Material3 components for input fields and checkbox
    - Buttons are enabled only when both username and password fields are non-empty
    - Password field validation shows error message if login fails
    - Default phone number is used for newly created users (phone # not used yet)
*/

package com.example.inventoryapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText usernameEdit;
    private TextInputEditText passwordEdit;
    private TextInputLayout usernameLayout;
    private TextInputLayout passwordLayout;
    private MaterialCheckBox rememberCheckbox;

    private InventoryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = InventoryDatabase.getInstance(this);

        // UI references
        usernameEdit = findViewById(R.id.username_edit_text);
        passwordEdit = findViewById(R.id.password_edit_text);
        usernameLayout = findViewById(R.id.username_input_layout);
        passwordLayout = findViewById(R.id.password_input_layout);
        rememberCheckbox = findViewById(R.id.remember_username_checkbox);

        Button signInButton = findViewById(R.id.sign_in_button);
        Button createAccountButton = findViewById(R.id.create_account_button);

        // Disable buttons initially
        signInButton.setEnabled(false);
        createAccountButton.setEnabled(false);

        // Watch text changes to enable buttons
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateButtonStates(signInButton, createAccountButton);
                usernameLayout.setError(null);
                passwordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameEdit.addTextChangedListener(watcher);
        passwordEdit.addTextChangedListener(watcher);

        // Load remembered username if any
        loadRememberedUsername();

        // Sign In button
        signInButton.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            // Check database for permissions
            if (db.validateUser(username, password)) {

                // Update remember checkbox on login
                if (rememberCheckbox.isChecked()) {
                    saveUsernameToPreferences(username);
                } else {
                    clearRememberedUsername();
                }

                // Update static session for this user
                AppSession.setCurrentUser(username);

                // Load and start activity
                Intent intent = new Intent(this, InventoryListActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Quick message login is incorrect
                passwordLayout.setError("Invalid username or password");
            }
        });

        // Create Account button
        createAccountButton.setOnClickListener(v -> {
            String username = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();

            // Check and warn before adding again
            if (db.userExists(username)) {
                usernameLayout.setError("User already exists");
                return;
            }

            // Add new user with default phone number
            db.addUser(username, password, false, "555-123-4567");

            usernameLayout.setError(null);
            passwordLayout.setError(null);
        });
    }

    // -----------------------------
    // Button state management
    // -----------------------------
    private void updateButtonStates(Button signIn, Button createAccount) {
        String username = Objects.requireNonNull(usernameEdit.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordEdit.getText()).toString().trim();

        boolean enabled = !username.isEmpty() && !password.isEmpty();
        signIn.setEnabled(enabled);
        createAccount.setEnabled(enabled);
    }

    // -----------------------------
    // Remember username helpers
    // -----------------------------
    private void saveUsernameToPreferences(String username) {
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        prefs.edit().putString("saved_username", username).apply();
    }

    private void loadRememberedUsername() {
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        String savedUser = prefs.getString("saved_username", null);

        if (savedUser != null) {
            usernameEdit.setText(savedUser);
            rememberCheckbox.setChecked(true);
        }
    }

    private void clearRememberedUsername() {
        SharedPreferences prefs = getSharedPreferences("login_prefs", MODE_PRIVATE);
        prefs.edit().remove("saved_username").apply();
    }
}
