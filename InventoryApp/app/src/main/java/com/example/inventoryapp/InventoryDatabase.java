/*
    File: InventoryDatabase.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: Provides SQLite database management for the InventoryApp.
             This class handles:
             - Inventory item storage and CRUD operations
             - User management, including login credentials and SMS preferences
             - Seeding default inventory items on first launch
             - SMS preference persistence per user
    Notes:
    - Uses singleton pattern via getInstance() to ensure single database connection.
    - ItemTable and UserTable define database schema constants for ease of use.
    - SMS functionality is limited to storing the user preference; sending is handled elsewhere.
    - initDefaultItems() ensures default items exist when the database is empty.
*/
package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class InventoryDatabase extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "inventory.db";

    private static InventoryDatabase mDb;

    public static InventoryDatabase getInstance(Context context) {
        if (mDb == null) {
            mDb = new InventoryDatabase(context.getApplicationContext());
        }
        return mDb;
    }

    private InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // -------------------------------------->
    // Item table
    // -----------------------------
    private static final class ItemTable {
        private static final String TABLE = "items";
        private static final String COL_ID = "_id";
        private static final String COL_NAME = "name";
        private static final String COL_QTY = "quantity";
    }

    // -----------------------------
    // User table
    // @note: phone isn't used at the moment
    // -----------------------------
    private static final class UserTable {
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_SMS_ENABLED = "sms_enabled"; // 0 = false, 1 = true
        private static final String COL_PHONE = "phone";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create Items table
        db.execSQL("CREATE TABLE " + ItemTable.TABLE + " (" +
                ItemTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ItemTable.COL_NAME + " TEXT, " +
                ItemTable.COL_QTY + " INTEGER)");

        // Create Users table
        db.execSQL("CREATE TABLE " + UserTable.TABLE + " (" +
                UserTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UserTable.COL_USERNAME + " TEXT UNIQUE, " +
                UserTable.COL_PASSWORD + " TEXT, " +
                UserTable.COL_SMS_ENABLED + " INTEGER DEFAULT 0, " +
                UserTable.COL_PHONE + " TEXT" +
                ")");

        // Seed inventory
        seedDefaultItems(db);

        // Default admin login
        // - or user can force their own credentials to be used through the login screen
        ContentValues user = new ContentValues();
        user.put(UserTable.COL_USERNAME, "admin");
        user.put(UserTable.COL_PASSWORD, "1234");
        db.insert(UserTable.TABLE, null, user);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ItemTable.TABLE);
        onCreate(db);
    }

    // -------- CRUD --------

    public List<Item> getItems() {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + ItemTable.TABLE, null);

        if (c.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(c.getInt(c.getColumnIndexOrThrow(ItemTable.COL_ID)));
                item.setName(c.getString(c.getColumnIndexOrThrow(ItemTable.COL_NAME)));
                item.setQuantity(c.getInt(c.getColumnIndexOrThrow(ItemTable.COL_QTY)));
                items.add(item);
            } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    // -------------------------------------------------------
    // Inventory management CRUD
    // -------------------------------------------------------

    public long addItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.COL_NAME, item.getName());
        cv.put(ItemTable.COL_QTY, item.getQuantity());
        return db.insert(ItemTable.TABLE, null, cv);
    }

    public void updateItem(Item item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ItemTable.COL_NAME, item.getName());
        cv.put(ItemTable.COL_QTY, item.getQuantity());

        db.update(ItemTable.TABLE, cv,
                ItemTable.COL_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(ItemTable.TABLE,
                ItemTable.COL_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    // -------------------------------------------------------
    // User management
    // -------------------------------------------------------

    public void addUser(String username, String password, boolean smsEnabled, String phone) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_USERNAME, username);
        cv.put(UserTable.COL_PASSWORD, password);
        cv.put(UserTable.COL_SMS_ENABLED, smsEnabled ? 1 : 0);
        cv.put(UserTable.COL_PHONE, phone);
        db.insert(UserTable.TABLE, null, cv);
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT * FROM users WHERE username=? AND password=?",
                new String[] { username, password }
        );

        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    public boolean userExists(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT * FROM users WHERE username=?",
                new String[] { username }
        );
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    // Get phone number for a specific user
    // (not used right now)
    public String getUserPhone(String username) {
        if (username == null) return null;

        SQLiteDatabase db = getReadableDatabase();
        String phone = null;

        Cursor c = db.rawQuery(
                "SELECT phone FROM users WHERE username = ?",
                new String[]{username}
        );

        if (c.moveToFirst()) {
            phone = c.getString(0);
        }

        c.close();
        return phone;
    }

    // -------------------------------------------------------
    // Database initialization & setup
    // -------------------------------------------------------
    public void initDefaultItems() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + ItemTable.TABLE, null);
        c.moveToFirst();
        int count = c.getInt(0);
        c.close();

        // Only write items if database is empty
        // - Either from startup, or user removing them all
        if (count == 0) {
            seedDefaultItems(db);
        }
    }

    // -------------------------------------------------------
    // SMS
    // -------------------------------------------------------

    // Get SMS preference for a user
    public boolean getSmsEnabled(String username) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(
                "SELECT " + UserTable.COL_SMS_ENABLED + " FROM " + UserTable.TABLE + " WHERE " + UserTable.COL_USERNAME + " = ?",
                new String[]{username});
        boolean enabled = false;
        if (c.moveToFirst()) {
            enabled = c.getInt(0) != 0;
        }
        c.close();
        return enabled;
    }

    // Set SMS preference for a user
    public void setSmsEnabled(String username, boolean enabled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(UserTable.COL_SMS_ENABLED, enabled ? 1 : 0);
        db.update(UserTable.TABLE, cv, UserTable.COL_USERNAME + " = ?", new String[]{username});
    }

    // -------------------------------------------------------
    // Seed default items to database (1st time or if empty)
    // -------------------------------------------------------

    private void seedDefaultItems(SQLiteDatabase db) {

        String[] names = {
                "Hammer",
                "Screwdriver Set",
                "Adjustable Wrench",
                "Cordless Drill",
                "Drill Bit Set",
                "Hand Saw",
                "Measuring Tape",
                "Flashlight",
                "Extension Cord",
                "Utility Knife",
                "Pliers",
                "Paint Roller",
                "Paint Tray",
                "Sandpaper Pack",
                "Safety Glasses",
                "Work Gloves",
                "Toolbox",
                "Level",
                "Socket Set",
                "Nails (Box of 100)"
        };

        int[] quantities = {
                15, 8, 10, 3, 12,
                5, 20, 7, 6, 14,
                9, 4, 6, 25, 13,
                18, 5, 9, 7, 40
        };

        ContentValues cv = new ContentValues();

        for (int i = 0; i < names.length; i++) {
            cv.clear();
            cv.put(ItemTable.COL_NAME, names[i]);
            cv.put(ItemTable.COL_QTY, quantities[i]);
            db.insert(ItemTable.TABLE, null, cv);
        }
    }
}
