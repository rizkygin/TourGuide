package com.example.tourguide.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourguide.Activity.AddDiscount;
import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.Activity.Merchant;
import com.example.tourguide.Activity.SpesificProduct;
import com.example.tourguide.R;
import com.example.tourguide.model.ItemOnMerchant;
import com.example.tourguide.model.MerchantShow;
import com.example.tourguide.model.Promo;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.JsonResponse;
import com.example.tourguide.service.SwipeToDeleteCallback;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemMerchant extends RecyclerView.Adapter<ItemMerchant.ViewHolder> {
    private static final String TAG = "ItemMerchant";

    String urlPhoto = null;
    Context mContext;

    private String token;
    private int merchant_id;
    private int path;
    private int pos;
    private String address;

    List<ItemOnMerchant> mData;
    public ItemMerchant() {
    }


    public ItemMerchant(Context mContext, List<ItemOnMerchant> mData, int path, String address) {
        this.mContext = mContext;
        this.mData = mData;
        this.path = path;
        this.address = address;
    }

    public Context getmContext() {
        return mContext;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
//        ViewHolder.getAdA.setText("Original Price " + result(value,originalPrice));
        SharedPreferences preferences = mContext.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        merchant_id = preferences.getInt("merchant_id", 0);
        token = preferences.getString("token","");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        if(merchant_id != 0 && merchant_id == path){
            holder.mButtonUpdate.setVisibility(View.VISIBLE);
        }
        else {
            holder.mButtonUpdate.setVisibility(View.GONE);
        }
//        holder.mName.setText(mData.get(position).getName() +" "+ mData.get(position).getId() + " " + " " + holder.getAdapterPosition() + " " +position);
        holder.mName.setText(mData.get(position).getName());
        urlPhoto = mData.get(position).getPhoto();
        Glide.with(mContext)
                .load(urlPhoto)
                .into(holder.mImage);
        holder.mPrice.setText("Rp. " + String.valueOf(mData.get(position).getPrice()));

        if(!mData.get(position).getPromo().isEmpty()){
            List<Promo> promos = mData.get(position).getPromo();
            Promo promo = mData.get(position).getPromo().get(promos.size()-1);
            int value = promo.getValue();
            int originalPrice = mData.get(position).getPrice();
            holder.mDisc.setText(String.valueOf(value));
//            holder.mOri.setText("Original Price " + result(value,originalPrice));
            holder.mPrice.setText("Rp. " + result(value,originalPrice));
            int positionswip = holder.getAdapterPosition();
            holder.mOri.setText("Rp " + originalPrice);
            holder.mRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SpesificProduct.class);
                    Bundle bundle = new Bundle();
                    String description = mData.get(position).getDescription();
                    bundle.putString("StringURLImage",mData.get(position).getPhoto());
                    Log.d(TAG, "onClick: StringURLImage "+ urlPhoto );
                    bundle.putString("StringDescription",description);
                    bundle.putString("StringAddress",address);
                    bundle.putInt("Value",mData.get(position).getPromo().get(0).getValue());
                    bundle.putString("StringEndDate",mData.get(position).getPromo().get(0).getEndDate());
                    bundle.putInt("merchantID",path);
                    bundle.putInt("pathItem",promo.getId());

                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });
        }else{
            holder.mRelative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "This Product Doesnt have any Promo yet", Toast.LENGTH_SHORT).show();
                }
            });
            holder.mbgDisc.setVisibility(View.GONE);
            holder.mDisc.setVisibility(View.GONE);
            holder.mOri.setVisibility(View.GONE);

        }

        //update per item
        holder.mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("StringURLImage",mData.get(position).getPhoto());
                bundle.putString("Mode","UpdateItem");
                bundle.putInt("Merhant_id",path);
                bundle.putInt("item_id",mData.get(position).getId());
                Intent intent = new Intent(mContext,AddDiscount.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


    }

    public void deleteItem(int position){
        pos = position;
//        ItemOnMerchant mRecentlyDeletedItem = mData.get(position);
//        mRecentlyDeletedItemPosition = position;

//        showUndoSnackbar();
        androidx.appcompat.app.AlertDialog.Builder builderItem = new AlertDialog.Builder(mContext);
        builderItem.setMessage("Dou want to delete " + mData.get(position).getName()  + " ?");
        builderItem.setCancelable(true);

        builderItem.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callRemoveApi(pos);

                        mData.remove(pos);
                        notifyItemRemoved(pos);
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

    private void callRemoveApi(int position) {
        Call<JsonResponse> call = Api.getClient().deleteItemMerhcant("Bearer "+ token,mData.get(position).getId());
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse item removed: true" );
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    private String result(int value, int originalPrice) {
        int result;
        result = originalPrice - (value/100 * originalPrice);
        return String.valueOf(result);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addPromo(int adapterPosition) {
        Intent intent = new Intent(mContext, AddDiscount.class);
        Bundle bundle = new Bundle();
        int item_id = mData.get(adapterPosition).getId();
        String photo = mData.get(adapterPosition).getPhoto();
        String description = mData.get(adapterPosition).getDescription();
        bundle.putString("StringURLImage",photo);
        bundle.putString("StringDescription",description);
        bundle.putInt("Item_id",item_id);
        bundle.putString("Mode","AddPromo");
        bundle.putInt("Merhant_id",path);
//        bundle.putString("StringAddress",address);
//        bundle.putInt("Value",mData.get(position).getPromo().get(0).getValue());
//        bundle.putString("StringEndDate",mData.get(position).getPromo().get(0).getEndDate());
//        bundle.putInt("merchantID",path);
//        bundle.putInt("pathItem",mData.get(position).getId());

        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout mRelative;
        private Button mButtonUpdate;
        private ImageView mImage;
        private ImageView mbgDisc;
        private CardView mitem;
        private TextView mName;
        private TextView mOri;
        private TextView mPrice;
        private TextView mDisc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mitem = itemView.findViewById(R.id.itemMerchant);
            mRelative = itemView.findViewById(R.id.listVoucher);
            mImage = itemView.findViewById(R.id.imItem1);
            mName = itemView.findViewById(R.id.nameItem);
            mOri = itemView.findViewById(R.id.oriPrice);
            mPrice = itemView.findViewById(R.id.priceNow);
            mDisc = itemView.findViewById(R.id.discount);
            mbgDisc = itemView.findViewById(R.id.bgDisc);
            mButtonUpdate = itemView.findViewById(R.id.btnUpdateItem);

        }
    }
}
