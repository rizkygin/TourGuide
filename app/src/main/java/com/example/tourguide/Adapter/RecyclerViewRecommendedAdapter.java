package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    int category;

    public RecyclerViewRecommendedAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public RecyclerViewRecommendedAdapter(Context mContext, List<Recommended> mData) {
        this.mContext = mContext;
        this.mData = mData;
        this.mDataAll = new ArrayList<>(mData);
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

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void getFilter(String categoryId){

        if(!mData.isEmpty()){
            if(mData.size() != mDataAll.size()){
                mData = mDataAll;
            }
            mDataFilter = new ArrayList<Recommended>();

            for (int i = 0; i< mData.size();i++){
                if(mData.get(i).getCategory_id() == Integer.parseInt(categoryId)){
                        mDataFilter.add(mData.get(i));
                    Log.d(TAG, "getFilter: "+ mData.size());
                }
            }
            if (mDataFilter.size() != 0){
                mData = mDataFilter;
                notifyDataSetChanged();
            }
        }
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
        private boolean mImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recommended = itemView.findViewById(R.id.recommended);
            mName = itemView.findViewById(R.id.recomended_name);
            mAddress = itemView.findViewById(R.id.recomended_addresss);

        }

    }


}
