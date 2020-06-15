package com.example.tourguide.service;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.example.tourguide.Activity.Merchant;
import com.example.tourguide.R;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.ItemMerchant;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private static final String TAG = "SwipCallback";

    private ItemMerchant mAdapter;
    private Drawable icon;
    private ColorDrawable backgroundLeft;
    private ColorDrawable backgroundRight;

    public SwipeToDeleteCallback(ItemMerchant adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        if(mAdapter.getmContext() != null){
            Log.d(TAG, "SwipeToDeleteCallback: is not Null");
            icon = ContextCompat.getDrawable(adapter.getmContext(),
                    R.drawable.ic_delete_sweep_black_24dp);
        }
        backgroundLeft = new ColorDrawable(Color.rgb(128,0,0));
        backgroundRight = new ColorDrawable(Color.rgb(50,205,50));

    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        //up and down movement
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//        int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){ //left Swiped
            mAdapter.deleteItem(viewHolder.getAdapterPosition() );
        }
        if(direction == ItemTouchHelper.RIGHT){//right Swiped
            mAdapter.updateActivity(viewHolder.getAdapterPosition());
            Log.d(TAG, "onSwiped: Right ?");
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20;


        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            int iconRight = itemView.getLeft() + iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundRight.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                    itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            backgroundLeft.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            backgroundLeft.setBounds(0, 0, 0, 0);
            backgroundRight.setBounds(0, 0, 0, 0);
        }

        backgroundLeft.draw(c);
        backgroundRight.draw(c);
        icon.draw(c);
    }
}
