package com.example.tourguide.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.R;

import java.util.List;

public class RecyclerViewRecommendedAdapter extends RecyclerView.Adapter<RecyclerViewRecommendedAdapter.ViewHolder> {

    Context mContext;
    List<Recommended> mData;

    public RecyclerViewRecommendedAdapter(Context mContext, List<Recommended> mData) {
        this.mContext= mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recomended_view,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mName.setText(mData.get(position).getName());
        holder.mAddress.setText(mData.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;
        private TextView mAddress;
        private boolean mImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.recomended_name);
            mAddress = itemView.findViewById(R.id.recomended_addresss);
        }
    }


}
