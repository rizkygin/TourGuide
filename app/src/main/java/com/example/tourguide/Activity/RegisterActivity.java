package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.example.tourguide.R;
import com.example.tourguide.model.RegisterdUser;
import com.example.tourguide.model.User;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.UserClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private AppCompatAutoCompleteTextView spinner;

    private TextInputLayout email,fullname,address,handphone,password;
    private String nametype,emailtype,passwordtype,handphonetype,addresstype;
    private String selectedOccupation;
    private MaterialButton signUp;

    private ProgressBar progressbar;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    UserClient userClient = retrofit.create(UserClient.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressbar = findViewById(R.id.progressbar);
        progressbar.setVisibility(View.GONE);

        email = findViewById(R.id.email);
        fullname = findViewById(R.id.fullname);
        address = findViewById(R.id.address);
        handphone = findViewById(R.id.handphone);
        password = findViewById(R.id.password);

        spinner = findViewById(R.id.dropdown);
        signUp = findViewById(R.id.btnSignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
                if(validate()){
                    register();
                }else{

                    Toast.makeText(RegisterActivity.this,"Upps Double Check the Form Registration",Toast.LENGTH_LONG).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.custom_spinner_dropdown,
                getResources().getStringArray(R.array.occupation)
        );
//        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOccupation = parent.getItemAtPosition(position).toString();
                Toast.makeText(RegisterActivity.this, selectedOccupation, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private boolean validate() {

        this.emailtype = email.getEditText().getText().toString();
        this.passwordtype = password.getEditText().getText().toString();
        this.nametype = fullname.getEditText().getText().toString();
        this.addresstype = address.getEditText().getText().toString();
        this.handphonetype = handphone.getEditText().getText().toString();

        if(emailtype.isEmpty() || isValidEmail(emailtype)){
            email.setError("Is Not Valid Email");
            return false;
        }
        if(passwordtype.isEmpty()){
            password.setError("Please Fill The Field");
            return false;
        }
        if(addresstype.isEmpty()){
            address.setError("Please Fill The Field");
            return false;
        }
        if(handphonetype.isEmpty()){
            handphone.setError("Please Fill The Field");
            return false;
        }
        if(nametype.isEmpty()){
            fullname.setError("Please Fill The Field");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String emailtype) {
        return !TextUtils.isEmpty(emailtype) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailtype).matches();
    }

    private void register() {
        Call<RegisterdUser> call =  userClient.registration(nametype,emailtype,passwordtype,passwordtype);
        call.enqueue(new Callback<RegisterdUser>() {
            @Override
            public void onResponse(Call<RegisterdUser> call, Response<RegisterdUser> response) {
                if(response.isSuccessful()){
                    SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                    Toast.makeText(RegisterActivity.this,"Registered Complete",Toast.LENGTH_LONG);
                    //Clear all UserData Shared Preferences
                    if(sharedPreferences.getString("email",null) != null){
                        sharedPreferences.edit()
                                .clear()
                                .commit();
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putString("email",emailtype);
                        editor.putString("name",nametype);
                        editor.putString("occupation",selectedOccupation);
                        editor.putString("address",addresstype);

                        editor.commit();
                    }else{
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putString("email",emailtype);
                        editor.putString("name",nametype);
                        editor.putString("occupation",selectedOccupation);
                        editor.putString("address",addresstype);

                        editor.commit();
                    }
                    Intent i = new Intent(RegisterActivity.this,SignInActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(RegisterActivity.this,"Upps Something Wrong",Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onFailure(Call<RegisterdUser> call, Throwable t) {
                String Message = t.getMessage();
//                Toast.makeText(SignInActivity.this,"Sorry Try Again" + emailtype + "/ " + "pass " + Message,Toast.LENGTH_LONG ).show();
////                Log.d("response", t.getStackTrace().toString());
                if (t instanceof SocketTimeoutException)
                {
                    Toast.makeText(RegisterActivity.this,"Connection time out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else if (t instanceof IOException)
                {
                    Toast.makeText(RegisterActivity.this,"Time Out  " + Message,Toast.LENGTH_LONG ).show();
                }
                else
                {
                    //Call was cancelled by user
                    if(call.isCanceled())
                    {
                        System.out.println("Call was cancelled forcefully");
                    }
                    else
                    {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });
    }


}
