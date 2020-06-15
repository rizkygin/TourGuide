package com.example.tourguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCity extends RecyclerView.Adapter<RecyclerCity.ViewHolder> {

    Context mContext;
    List<String> mData;

    public RecyclerCity(Context mContext, List<String> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_city,parent,false);
        final ViewHolder viewHolder = new ViewHolder(v);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.city.setText(mData.get(position).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.CityList);
        }
    }

    public void filterList(ArrayList<String> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}
