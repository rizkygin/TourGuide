package com.example.tourguide.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.R;
import com.example.tourguide.model.RedeemResponses;
import com.example.tourguide.model.Reward;
import com.example.tourguide.service.Api;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardHolder> {

    Context mContext;
    List<Reward> mData;
    int id_promo;
    String userPoint;
    String token;
    View dialog;
    AlertDialog.Builder builder;
    public RewardAdapter() {
    }

    public RewardAdapter(Context mContext, List<Reward> mData,String userPoint,String token) {
        this.mContext = mContext;
        this.mData = mData;
        this.userPoint = userPoint;
        this.token = token;
    }
    @NonNull
    @Override
    public RewardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.list_reward,parent,false);
        RewardAdapter.RewardHolder rewardHolder = new RewardAdapter.RewardHolder(v);
        builder = new AlertDialog.Builder(mContext);
        dialog = LayoutInflater.from(mContext).inflate(R.layout.custom_dialog_redeem,parent,false);
        return rewardHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RewardHolder holder, int position) {
        holder.eRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO call apiHome
                id_promo = mData.get(position).getId();
                if(Integer.parseInt(userPoint) >= mData.get(position).getPoint() ){
                    callApiRedeem(id_promo,position);
                }else{
                    Toast.makeText(mContext, "Not enough Point!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(userPoint == null){
            holder.eRedeem.setVisibility(View.GONE);
        }

        holder.points.setText(String.valueOf(mData.get(position).getPoint()));
        holder.name.setText(mData.get(position).getName());
        holder.description.setText(mData.get(position).getDescription());
        Glide.with(mContext)
                .load(mData.get(position).getPhoto())
                .into(holder.mPic);

    }

    private void callApiRedeem(int id_promo,int position) {
        Call<RedeemResponses> redeemCall = Api.getClient().redeem("Bearer " + token,id_promo);
        redeemCall.enqueue(new Callback<RedeemResponses>() {
            @Override
            public void onResponse(Call<RedeemResponses> call, Response<RedeemResponses> response) {
                if(response.isSuccessful()){

                    int user = Integer.parseInt(userPoint);
                    int results = user - response.body().getData().getPoint();


                    SharedPreferences.Editor editor = mContext.getSharedPreferences("UserData",Context.MODE_PRIVATE).edit();
                    editor.putString("points",String.valueOf(results));
                    editor.commit();

                    TextView name = dialog.findViewById(R.id.mName);
                    name.setText(response.body().getData().getName());
                    TextView description = dialog.findViewById(R.id.info);
                    description.setText(response.body().getData().getDescription());
                    ImageView image = dialog.findViewById(R.id.imageViewRedeem);
                    Glide.with(mContext).load(mData.get(position).getPhoto()).into(image);
                    Button mButton = dialog.findViewById(R.id.buttonOk);

                    builder.setView(dialog);
                    AlertDialog alertDialog = builder.create();
                    mButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(mContext, "You have got " + response.body().getData().getName() + "!. Thank you.", Toast.LENGTH_LONG).show();
                            mContext.startActivity(new Intent(mContext, LandingMainActivity.class));
                        }
                    });
                    alertDialog.show();
                }

            }

            @Override
            public void onFailure(Call<RedeemResponses> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class RewardHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView description;
        TextView points;
        Button eRedeem;
        ImageView mPic;

        public RewardHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameStore);
            description = itemView.findViewById(R.id.descriptionList);
            points = itemView.findViewById(R.id.points);
            eRedeem = itemView.findViewById(R.id.btnGetVoucher);
            mPic = itemView.findViewById(R.id.photoImageReward);
        }
    }
}
