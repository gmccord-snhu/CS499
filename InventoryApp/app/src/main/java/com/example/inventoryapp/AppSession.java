/*
    File: AppSession.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Provides a simple static session manager for the application.
             This class tracks:
             - The current logged-in user.
             - The SMS badge count used in the toolbar.
    Notes:
    - All fields and methods are static for easy global access.
    - Badge count can be incremented, set, or reset programmatically.
    - Does not persist data; intended for in-memory session management only.

    Implementation Details:
    - The SMS "messages" in the app are simulated via the static badge counter.
    - When the ViewModel detects low inventory items, it increments this counter
      to simulate the arrival of a new low-inventory notification.
*/
package com.example.inventoryapp;

public class AppSession {

    private static String currentUser;
    private static int smsBadgeCount = 0;

    // ----------------------------
    // User session
    // ----------------------------
    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    // ----------------------------
    // SMS badge count
    // ----------------------------
    public static int getSmsBadgeCount() {
        return smsBadgeCount;
    }

    public static void setSmsBadgeCount(int count) {
        smsBadgeCount = count;
    }

    public static void resetSmsBadgeCount() {
        smsBadgeCount = 0;
    }
}
