/*
    File: ItemAdapter.java
    Author: Grant McCord
    Class: CS 360 – Mobile App Development
    Date: 2025-12-13
    Purpose: RecyclerView Adapter for displaying inventory items in a list.
             Responsibilities:
             - Bind item data (name, quantity, status) to the card layout
             - Update status pill color and text based on quantity
             - Handle edit button click via callback
             - Dynamically load item images (currently static placeholder)
    Notes:
    - Uses Consumer<Item> callback for edit actions
    - Uses Runnable callback to refresh UI after data changes
    - Layout file used: inventory_card_item.xml
    - Status pill colors: In Stock (green), Low Stock (orange), Out of Stock (red)
*/
package com.example.inventoryapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> items;
    private final InventoryViewModel viewModel;
    private final Consumer<Item> onEditClick; // callback for edit
    private final Runnable onDataChanged;     // callback to refresh UI after changes

    public ItemAdapter(List<Item> items, InventoryViewModel viewModel,
                       Consumer<Item> onEditClick, Runnable onDataChanged) {
        this.items = items;
        this.viewModel = viewModel;
        this.onEditClick = onEditClick;
        this.onDataChanged = onDataChanged;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_card_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);

        holder.itemName.setText(item.getName());
        holder.quantityText.setText(String.valueOf(item.getQuantity()));

        // Status pill
        if (item.getQuantity() == 0) {
            holder.statusPill.setText("Out of Stock");
            holder.statusPill.setBackgroundResource(R.drawable.pill_out_of_stock);
            holder.statusPill.setTextColor(holder.itemView.getResources().getColor(android.R.color.holo_red_dark));
        } else if (item.getQuantity() <= 5) {
            holder.statusPill.setText("Low Stock");
            holder.statusPill.setBackgroundResource(R.drawable.pill_low_stock);
            holder.statusPill.setTextColor(holder.itemView.getResources().getColor(android.R.color.holo_orange_dark));
        } else {
            holder.statusPill.setText("In Stock");
            holder.statusPill.setBackgroundResource(R.drawable.pill_instock);
            holder.statusPill.setTextColor(holder.itemView.getResources().getColor(android.R.color.holo_green_dark));
        }

        holder.itemImage.setImageResource(R.drawable.box);

        // Edit button
        holder.editMenuButton.setOnClickListener(v -> onEditClick.accept(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemName;
        TextView quantityText;
        TextView statusPill;
        ImageButton editMenuButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemName = itemView.findViewById(R.id.item_name_text);
            quantityText = itemView.findViewById(R.id.quantity_text);
            statusPill = itemView.findViewById(R.id.status_pill);
            editMenuButton = itemView.findViewById(R.id.edit_menu_button);
        }
    }
}
