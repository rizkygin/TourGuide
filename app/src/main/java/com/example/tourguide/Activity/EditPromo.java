package com.example.tourguide.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tourguide.R;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.JsonResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPromo extends AppCompatActivity {

    String token;
    String item_id;
    String rCallDescription;
    String rCallValue;
    String rCallCategory = "discount";
    String rCallMax_cut;
    String rCallStart_time;
    String rCallEnd_time;
    String rCallMethod = "PUT";

    TextInputLayout mDesc,mValue,mMaxCut;
    TextView mStartDate,mEndDate,helperStartTime,helperEndTime;
    MaterialButton mButtonSave;
    ProgressBar mProgress;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calendar=  Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_promo);

        mDesc = findViewById(R.id.epDescription);
        mValue = findViewById(R.id.epValue);
        mMaxCut = findViewById(R.id.epMaxCut);
        mStartDate = findViewById(R.id.mStartDateFix);
        mEndDate = findViewById(R.id.endDateFix);
        helperStartTime = findViewById(R.id.helperTextSetting);
        helperEndTime = findViewById(R.id.helperTextSettingEnd);

        mProgress = findViewById(R.id.epProgressBar);
        mButtonSave = findViewById(R.id.epSaveBtn);

        Intent intent = getIntent();
        item_id = intent.getExtras().getString("id");
        mDesc.getEditText().setText(intent.getExtras().getString("Description"));
        mValue.getEditText().setText(intent.getExtras().getString("value"));
        mMaxCut.getEditText().setText(intent.getExtras().getString("max_cut"));
        //Call SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        String endDate = intent.getExtras().getString("Start_Date");
        Date start = Calendar.getInstance().getTime();
        try {
            start = formatter.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mStartDate.setText(formatter.format(start));
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditPromo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String startDate;
                        month = month+1;
                        String date = year+"-"+month+"-"+dayOfMonth;
                        startDate = date;
                        mStartDate.setText(startDate);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        final int yearEnd = calendar.get(Calendar.YEAR);
        final int monthEnd = calendar.get(Calendar.MONTH);
        final int dayEnd = calendar.get(Calendar.DAY_OF_MONTH);
        String end_date = intent.getExtras().getString("End_Date");
        Date end = Calendar.getInstance().getTime();
        try {
            end = formatter.parse(end_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mEndDate.setText(formatter.format(end));
        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditPromo.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String endDate;
                        month = month+1;
                        String date = year+"-"+month+"-"+dayOfMonth;
                        endDate = date;
                        mEndDate.setText(endDate);
                    }
                }, yearEnd, monthEnd, dayEnd);
                datePickerDialog.show();
            }
        });
        mProgress.setVisibility(View.GONE);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(validate()){
                        mProgress.setVisibility(View.VISIBLE);
                        mButtonSave.setClickable(false);
                        updatePromo();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void updatePromo() {

        Call<JsonResponse> call = Api.getClient().promoUpdate("Bearer "+ token,rCallValue,rCallDescription,rCallCategory,rCallMax_cut,rCallStart_time,rCallEnd_time,item_id,rCallMethod);
        call.enqueue(new Callback<JsonResponse>() {
            @Override
            public void onResponse(Call<JsonResponse> call, Response<JsonResponse> response) {
                if(response.isSuccessful()){
                     mProgress.setVisibility(View.GONE);
                    androidx.appcompat.app.AlertDialog.Builder builderItem = new AlertDialog.Builder(EditPromo.this);

                    builderItem.setMessage("You have been success to update! :D");
                    builderItem.setCancelable(true);

                    builderItem.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                        Intent intent2 = new Intent(EditPromo.this,CheckPromoMerchant.class);
                                        startActivity(intent2);
                                }
                            });
                    AlertDialog alert11 = builderItem.create();
                    alert11.show();
                }
            }

            @Override
            public void onFailure(Call<JsonResponse> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                mButtonSave.setClickable(true);

                Toast.makeText(EditPromo.this, "Message Problem " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() throws ParseException {
        rCallDescription = mDesc.getEditText().getText().toString();
        rCallValue = mValue.getEditText().getText().toString();
        rCallMax_cut = mMaxCut.getEditText().getText().toString();
        rCallStart_time = mStartDate.getText().toString();
        rCallEnd_time = mEndDate.getText().toString();

        Date start = formatter.parse(rCallStart_time);
        Date end = formatter.parse(rCallEnd_time);

        if(start.compareTo(end) > 0){
            helperEndTime.setText("You cant set start Time greater than the end time");
            helperEndTime.setTextColor(getResources().getColor(R.color.warning));
            return false;
        }
        if(rCallDescription.isEmpty()){
            mDesc.setError("Please fill the field");
            return false;
        }if(rCallValue.isEmpty()){
            mValue.setError("Please fill the field");
            return false;
        }
        if(rCallValue != null){
            if(Integer.parseInt(rCallValue) > 100) {
                mValue.setError("Enter under 100 % value");
                return false;
            }
        }
        if(rCallMax_cut.isEmpty()){
            mMaxCut.setError("Please fill the field");
            return false;
        }if(rCallStart_time.isEmpty()){
            mStartDate.setError("Please fill the field");
            return false;
        }if(rCallEnd_time.isEmpty()){
            mEndDate.setError("Please fill the field");
            return false;
        }
        return  true;
    }
}
