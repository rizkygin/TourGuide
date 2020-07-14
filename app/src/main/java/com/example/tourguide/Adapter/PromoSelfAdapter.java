package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromoSelfAdapter extends RecyclerView.Adapter<PromoSelfAdapter.PromoHolder> {

    private static final String TAG = "Promo Self";
    List<PromoSelfCheck> mData;
    Context mContext;
    String token;
    SimpleDateFormat formatter,output;
    Date date,now,hPlus1;
    Calendar calWeek = Calendar.getInstance();
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
        formatter= new SimpleDateFormat("yyyy-MM-dd");
        output = new SimpleDateFormat("MMMM, dd yyyy");
        now = new Date();
        calWeek.add(Calendar.DATE,+1);
        hPlus1 = calWeek.getTime();
        return promoHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PromoHolder holder, int position) {

        holder.pName.setText(mData.get(position).getDescription());
        try {
             date = formatter.parse(mData.get(position).getEnd_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(output.format(date).equals(output.format(hPlus1))){
            holder.pValid.setTextColor(mContext.getResources().getColor(R.color.warning));
            holder.pValid.setText("Experied on tommorow ");

        }
        Log.d(TAG, "onBindViewHolder: "+ hPlus1) ;
        Log.d(TAG, "onBindViewHolder:date "+ date) ;
        Log.d(TAG, "onBindViewHolder: format "+ output.format(hPlus1)) ;
        Log.d(TAG, "onBindViewHolder:date format "+ output.format(hPlus1)) ;
        if(output.format(date).equals(output.format(now))){
            holder.pValid.setTextColor(mContext.getResources().getColor(R.color.warning_high));
            holder.pValid.setText("Experied today ");

        }
        holder.pEnd.setText(output.format(date));
        holder.pValueDiscount.setText(mData.get(position).getValue() + "%");
        holder.pUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent update = new Intent(mContext, EditPromo.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",String.valueOf(mData.get(position).getId()));
                bundle.putString("Description",String.valueOf(mData.get(position).getDescription()));
                bundle.putString("value",String.valueOf(mData.get(position).getValue()));
                bundle.putString("max_cut",String.valueOf(mData.get(position).getMax_cut()));
                bundle.putString("Start_Date",String.valueOf(mData.get(position).getStart_time()));
                bundle.putString("End_Date",String.valueOf(mData.get(position).getEnd_time()));
                update.putExtras(bundle);
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
                builderItem.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notifyDataSetChanged();
                        Toast.makeText(mContext, "You cancelled your action", Toast.LENGTH_SHORT).show();
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
        TextView pValid;
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
            pValid = itemView.findViewById(R.id.psLabelEndDate);

        }
    }
}
