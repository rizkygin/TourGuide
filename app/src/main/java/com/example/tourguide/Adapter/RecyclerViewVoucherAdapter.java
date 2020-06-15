package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Activity.SpesificProduct;
import com.example.tourguide.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class RecyclerViewVoucherAdapter extends RecyclerView.Adapter<RecyclerViewVoucherAdapter.VoucherHolder> {
    Context mContext;
    List<Voucher> mData;

    public RecyclerViewVoucherAdapter() {
    }

    public RecyclerViewVoucherAdapter(Context mContext, List<Voucher> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public VoucherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_voucher,parent,false);
        VoucherHolder voucherHolder = new VoucherHolder(v);
        return voucherHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final VoucherHolder holder, final int position) {
        holder.nameStore.setText(mData.get(position).getDescription());
        holder.nominal.setText(String.valueOf(mData.get(position).getValue()) + " %");
        holder.mEndDate.setText(mData.get(position).getEnd_time());

        holder.claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mEndDate.setText("Claimed");
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VoucherHolder extends  RecyclerView.ViewHolder{

        private RelativeLayout mRelative;
        private TextView nominal;
        private TextView nameStore;
        private TextView mEndDate;
        private Button claim;
        public VoucherHolder(@NonNull View itemView) {
            super(itemView);

            claim = itemView.findViewById(R.id.btnGetVoucher);
            mRelative = itemView.findViewById(R.id.listVoucher);
            mEndDate = itemView.findViewById(R.id.endDate);
            nameStore = itemView.findViewById(R.id.nameStore);
            nominal = itemView.findViewById(R.id.voucherNominal);
        }
    }
}
