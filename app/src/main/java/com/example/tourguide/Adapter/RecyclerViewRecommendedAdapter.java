package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.Activity.ListenerFilterRecommended;
import com.example.tourguide.Activity.LoginActivity;
import com.example.tourguide.Activity.Merchant;
import com.example.tourguide.Activity.SplashScreen;
import com.example.tourguide.Activity.ui.home.AdapterListenerFilter;
import com.example.tourguide.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerViewRecommendedAdapter extends RecyclerView.Adapter<RecyclerViewRecommendedAdapter.ViewHolder> {

    private static final String TAG = "RecommendedAdapter";
    Context mContext;
    List<Recommended> mData;
    List<Recommended> mDataAll;
    List<Recommended> mDataFilter = new ArrayList<>();
    int categoryId;

    int category;

    public RecyclerViewRecommendedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerViewRecommendedAdapter(Context mContext, List<Recommended> mData,int categoryId) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataAll = new ArrayList<>(mData);
        this.categoryId = categoryId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recomended_view,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);

        viewHolder.recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Merchant.class);

                Bundle bundle = new Bundle();
                bundle.putInt("idMerchant" , mData.get(viewHolder.getAdapterPosition()).getId());

                intent.putExtras(bundle);
                mContext.startActivity(intent);

                Log.d("testAdapter ", String.valueOf(mData.get(viewHolder.getAdapterPosition()).getId()));
//                Log.d("testAdapter ", String.valueOf(mData.size()));
                Log.d("testAdapter", "Context Adapter " + mContext);

            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.mAddress.setText(mData.get(position).getAddress());

        switch (categoryId){
            case 1:
                holder.category.setText("Souvenir");
                holder.category.setTextColor(mContext.getResources().getColor(R.color.bg_souvenir));
                holder.category.setBackground(mContext.getResources().getDrawable(R.drawable.bg_souvenir));

                break;
            case 2:
                holder.category.setText("Hotel");
                holder.category.setTextColor(mContext.getResources().getColor(R.color.bg_hotel));
                holder.category.setBackground(mContext.getResources().getDrawable(R.drawable.bg_hotel));


                break;
            case 3:
                holder.category.setText("Food");
                holder.category.setTextColor(mContext.getResources().getColor(R.color.bg_food));
                holder.category.setBackground(mContext.getResources().getDrawable(R.drawable.bg_food));


                break;
            case 5:
                holder.category.setText("Recommended");
                holder.category.setTextColor(mContext.getResources().getColor(R.color.bg_recommended));
                holder.category.setBackground(mContext.getResources().getDrawable(R.drawable.bg_recommended));

                break;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public void getResults(ArrayList<Recommended> filter) {
        mData = filter;
        Log.d(TAG, "getResults: Seharusnya berubah :)");
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView recommended;
        private TextView mName;
        private TextView mAddress;
        Button category;
        private boolean mImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recommended = itemView.findViewById(R.id.recommended);
            mName = itemView.findViewById(R.id.recomended_name);
            mAddress = itemView.findViewById(R.id.recomended_addresss);
            category = itemView.findViewById(R.id.tourism_image);

        }

    }


}
