package com.example.tourguide.service;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.ItemMerchant;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private ItemMerchant mAdapter;

    public SwipeToDeleteCallback(ItemMerchant adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(viewHolder.getAdapterPosition() );
    }
}
