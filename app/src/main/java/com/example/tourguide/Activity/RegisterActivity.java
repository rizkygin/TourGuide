package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.example.tourguide.R;
import com.example.tourguide.model.RegisterdUser;
import com.example.tourguide.model.User;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.GMailSender;
import com.example.tourguide.service.UserClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
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
    private MaterialButton signUp,openEmailButton;

    private ImageView mOpenEmailBG;

    private ProgressBar progressbar;
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://tourgr.id/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
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
        handphone = findViewById(R.id.handphone);
        password = findViewById(R.id.password);

        signUp = findViewById(R.id.btnSignUp);
        mOpenEmailBG = findViewById(R.id.openEmailBG);
        openEmailButton = findViewById(R.id.openEmailButton);

        openEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this,SignInActivity.class);
                startActivity(i);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar.setVisibility(View.VISIBLE);
//                //TODO Checking the email sender by Gmail
//                try {
//                    GMailSender sender = new GMailSender("riwayathiduporang@gmail.com", "TRI16@ISec");
//                    sender.sendMail("Test Api App",
//                            "This is Testing",
//                            "riwayathiduporang@gmail.com",
//                            "riwayathiduporang@gmail.com");
//                    progressbar.setVisibility(View.GONE);
//                    Log.d("SendMail", "onClick: What");
//
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
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
    }

    private boolean validate() {

        this.emailtype = email.getEditText().getText().toString();
        this.passwordtype = password.getEditText().getText().toString();
        this.nametype = fullname.getEditText().getText().toString();
        this.handphonetype = handphone.getEditText().getText().toString();

        if(emailtype.isEmpty()){
            email.setError("Is Not Valid Email!");
            return false;
        }
        if(passwordtype.isEmpty()){
            password.setError("Please Fill The Field!");
            return false;
        }
        if(passwordtype.length() < 6){
            password.setError("At least 6 character for make password!");
            return false;
        }
        if(handphonetype.isEmpty()){
            handphone.setError("Please Fill The Field!");
            return false;
        }
        if(nametype.isEmpty()){
            fullname.setError("Please Fill The Field!");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String emailtype) {
        return !TextUtils.isEmpty(emailtype) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailtype).matches();
    }

    private void register() {
        Call<RegisterdUser> call =  userClient.registration(nametype,emailtype,passwordtype,handphonetype,passwordtype);
        call.enqueue(new Callback<RegisterdUser>() {
            @Override
            public void onResponse(Call<RegisterdUser> call, Response<RegisterdUser> response) {
                if(response.isSuccessful()){
//                    SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                    Toast.makeText(RegisterActivity.this,"Registered Complete",Toast.LENGTH_LONG);
                    //Clear all UserData Shared Preferences
//                    if(sharedPreferences.getString("email",null) != null){
//                        sharedPreferences.edit()
//                                .clear()
//                                .commit();
//                        SharedPreferences.Editor editor= sharedPreferences.edit();
//                        editor.putString("email",emailtype);
//                        editor.putString("name",nametype);
//                        editor.putString("phone",handphonetype);
//
//                        editor.commit();
//                    }else{
//                        SharedPreferences.Editor editor= sharedPreferences.edit();
//                        editor.putString("email",emailtype);
//                        editor.putString("name",nametype);
//                        editor.putString("phone",handphonetype);
//
//                        editor.commit();
//                    }
                    //close the keyboard
                    View view = RegisterActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(RegisterActivity.this.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    openEmailButton.setVisibility(View.VISIBLE);
                    mOpenEmailBG.setVisibility(View.VISIBLE);
                }else{
                    email.setError("This email has already been taken");
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this,"Something Wrong",Toast.LENGTH_LONG);
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
