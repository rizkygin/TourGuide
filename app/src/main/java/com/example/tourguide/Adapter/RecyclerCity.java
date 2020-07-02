package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.City;
import com.example.tourguide.R;
import com.example.tourguide.service.CityHelper;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.city.setText(mData.get(position).toUpperCase());
        holder.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent city = new Intent(mContext, LandingMainActivity.class);
//                CityHelper cityhelper = new CityHelper(mData.get(position));
                city.putExtra("SearchedCity",mData.get(position));
                SharedPreferences.Editor editor = mContext.getSharedPreferences("UserData", Context.MODE_PRIVATE).edit();
                editor.putString("SearchedCity", mData.get(position));
                if(mData.get(position).equals("Anywhere")){
                    editor.putString("SearchedCity", null);
                }
                editor.commit();
                mContext.startActivity(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout click;
        TextView city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            city = itemView.findViewById(R.id.CityList);
            click= itemView.findViewById(R.id.linear);
        }
    }

    public void filterList(ArrayList<String> filteredList) {
        mData = filteredList;
        notifyDataSetChanged();
    }
}
