package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Activity.CheckPromoMerchant;
import com.example.tourguide.Activity.EditPromo;
import com.example.tourguide.model.PromoSelfCheck;
import com.example.tourguide.R;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.JsonResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoSelfAdapter extends RecyclerView.Adapter<PromoSelfAdapter.PromoHolder> {

    List<PromoSelfCheck> mData;
    Context mContext;
    String token;

    public PromoSelfAdapter(List<PromoSelfCheck> mData, Context mContext,String token) {
        this.mData = mData;
        this.mContext = mContext;
        this.token = token;
    }

    @NonNull
    @Override
    public PromoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_promo_self,parent,false);
        final PromoHolder promoHolder = new PromoHolder(v);
        return promoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromoHolder holder, int position) {

        holder.pName.setText(mData.get(position).getDescription());
        holder.pEnd.setText(mData.get(position).getEnd_time());
        holder.pValueDiscount.setText(mData.get(position).getValue() + "%");
        holder.pUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(mContext, EditPromo.class);
                update.putExtra("id",String.valueOf(mData.get(position).getId()));
                mContext.startActivity(update);
            }
        });
        holder.pDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                androidx.appcompat.app.AlertDialog.Builder builderItem = new AlertDialog.Builder(mContext);

                builderItem.setMessage("Are you really want delete this promo ? :(");
                builderItem.setCancelable(true);

                builderItem.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                destroyItem(position);
                            }
                        });
                AlertDialog alert11 = builderItem.create();
                alert11.show();
            }
        });

    }

    private void destroyItem(int position) {

        Call<JsonResponse> destroy = Api.getClient().destroyPromo("Bearer " + token,mData.get(position).getId());
        destroy.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    mData.remove(position);
                    notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class PromoHolder extends RecyclerView.ViewHolder {

        TextView pName;
        TextView pDescription;
        TextView pEnd;
        TextView pValueDiscount;
        Button pUpdate;
        Button pDestroy;
        public PromoHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.psName);
            pDescription = itemView.findViewById(R.id.psDesc);
            pValueDiscount = itemView.findViewById(R.id.psDiscount);
            pEnd = itemView.findViewById(R.id.psEnd);
            pUpdate = itemView.findViewById(R.id.psUpdateBtn);
            pDestroy = itemView.findViewById(R.id.psDestroyBtn);

        }
    }
}
