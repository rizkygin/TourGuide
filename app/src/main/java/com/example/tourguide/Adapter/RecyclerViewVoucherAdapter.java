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
    public void onBindViewHolder(@NonNull VoucherHolder holder, int position) {
        holder.nameStore.setText(mData.get(position).getNameStore());
        holder.nominal.setText(mData.get(position).getNominal().toString());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VoucherHolder extends  RecyclerView.ViewHolder{

        private TextView nominal;
        private TextView nameStore;
        public VoucherHolder(@NonNull View itemView) {
            super(itemView);

            nameStore = itemView.findViewById(R.id.nameStore);
            nominal = itemView.findViewById(R.id.voucherNominal);
        }
    }
}
