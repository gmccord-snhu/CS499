package com.example.inventoryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.*;

import java.util.ArrayList;
import java.util.List;

public class ReportsActivity extends AppCompatActivity {

    private InventoryViewModel viewModel;
    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        // Init ViewModel (same as your inventory screen)
        viewModel = new InventoryViewModel(getApplication());

        // Chart reference
        barChart = findViewById(R.id.barChart);

        setupChart();

        // Bottom navigation
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.nav_reports);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inventory) {
                startActivity(new Intent(this, InventoryListActivity.class));
                return true;
            }

            if (id == R.id.nav_reports) return true;

            if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SmsSettingsActivity.class));
                return true;
            }

            return false;
        });
    }

    private void setupChart() {

        List<Item> items = viewModel.getItems();

        int inStock = 0;
        int lowStock = 0;
        int outOfStock = 0;

        for (Item item : items) {
            int qty = item.getQuantity();

            if (qty == 0) {
                outOfStock++;
            } else if (qty <= 5) {
                lowStock++;
            } else {
                inStock++;
            }
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, inStock));
        entries.add(new BarEntry(1, lowStock));
        entries.add(new BarEntry(2, outOfStock));

        BarDataSet dataSet = new BarDataSet(entries, "Inventory Status");

        dataSet.setColors(
                android.graphics.Color.GREEN,
                android.graphics.Color.YELLOW,
                android.graphics.Color.RED
        );

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);

        // Labels
        String[] labels = {"In Stock", "Low", "Out"};

        barChart.getXAxis().setValueFormatter(
                new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(labels)
        );
        barChart.getXAxis().setGranularity(1f);

        barChart.getDescription().setEnabled(false);
        barChart.getAxisRight().setEnabled(false);

        barChart.invalidate();
    }
}