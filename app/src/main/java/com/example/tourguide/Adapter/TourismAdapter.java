package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.tourguide.Activity.GalleryActivity;
import com.example.tourguide.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.model.TourismIndexGet;

import java.util.List;

public class TourismAdapter extends RecyclerView.Adapter<TourismAdapter.TourViewHolder> {

    Context mContext;
    List<TourismIndexGet> mData;

    public TourismAdapter(Context mContext, List<TourismIndexGet> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public TourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_tourism,parent,false);
        TourViewHolder tourViewHolder = new TourViewHolder(v);
        return tourViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TourViewHolder holder, int position) {

        holder.mName.setText(mData.get(position).getName());
        holder.mAddress.setText(mData.get(position).getAddress());
        holder.mClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GalleryActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("idTourism", mData.get(position).getId());
                bundle.putString("nameTourism", mData.get(position).getName());
                bundle.putString("description", mData.get(position).getDescription());
                bundle.putString("location", mData.get(position).getAddress());
                bundle.putDouble("latitude", mData.get(position).getLatitude());
                bundle.putDouble("longitude", mData.get(position).getLongitude());

                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TourViewHolder extends RecyclerView.ViewHolder {
        CardView mClick;
        TextView mName;
        TextView mAddress;
        public TourViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.tourism_name);
            mAddress = itemView.findViewById(R.id.tourism_address);
            mClick = itemView.findViewById(R.id.tourism);
        }
    }
}
