package com.example.tourguide.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.tourguide.Activity.ui.home.HomeFragment;
import com.example.tourguide.model.MerchantShow;
import com.example.tourguide.model.User;
import com.example.tourguide.service.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.tourguide.R;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,ListenerFilterRecommended {

    private AppBarConfiguration mAppBarConfiguration;
//    private GoogleMap mMap;


    static String cityComplete;
    int merchantLogin = 0;
    String token;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    ImageView constraintLayoutNav;
    MaterialButton searchView;
    MaterialButton mOpenMerchant;

    TextView mNameUser;
    String nameUser;

    private String TAG = "LandingMainActivity";
    private int mInterval = 5000; // 5 seconds by default, can be changed later
    private Handler mHandler;

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRepeatingTask();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus(); //this function can change value of mInterval.
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(mStatusChecker, mInterval);
            }
        }
    };



    void startRepeatingTask() {
        mStatusChecker.run();
    }

    void stopRepeatingTask() {
        mHandler.removeCallbacks(mStatusChecker);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent city = getIntent();

        mOpenMerchant = findViewById(R.id.openMerchant);
        searchView = findViewById(R.id.search_view);
        cityComplete = getSharedPreferences("UserData",Context.MODE_PRIVATE).getString("SearchedCity","Search City");
        searchView.setText(cityComplete);

        mHandler = new Handler();
        startRepeatingTask();

        SharedPreferences sharedPreferences = getSharedPreferences("UserData",Context.MODE_PRIVATE);
        merchantLogin = sharedPreferences.getInt("merchant_id",0);
        token = sharedPreferences.getString("token","");
        nameUser = sharedPreferences.getString("name","Travel Guide");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragmenlt) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        Action
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(LandingMainActivity.this,SearchCityActivity.class);

                startActivity(search);
            }
        });




        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.getMenu().getItem(1).getItemId();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setDrawerLayout(drawerLayout)
                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        constraintLayoutNav = navHeader.findViewById(R.id.image_cover_merchant);
        mNameUser = navHeader.findViewById(R.id.username);
        mNameUser.setText(nameUser);
        navigationView.getMenu().getItem(1).setVisible(false);
        navigationView.getMenu().getItem(0).setVisible(true);
        if(merchantLogin != 0 ){

            navigationView.getMenu().getItem(1).setVisible(true);
            navigationView.getMenu().getItem(0).setVisible(false);
            mOpenMerchant.setVisibility(View.VISIBLE);
            mOpenMerchant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LandingMainActivity.this, Merchant.class);

                    Bundle bundle = new Bundle();
                    bundle.putInt("idMerchant" , merchantLogin);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            Glide.with(LandingMainActivity.this)
                    .load(sharedPreferences.getString("imageMerchant",""))
                    .into(constraintLayoutNav);

        }


        if(merchantLogin!=0){
            navigationView.getMenu().getItem(1).setVisible(true);

        }
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        FragamentFilter fragamentFilter= new FragamentFilter();
//        fragmentTransaction.add(R.id.fragmentFilter,fragamentFilter);
//        fragmentTransaction.commit();
////
//        FragmentManager homeFragment = getSupportFragmentManager();
//        FragmentTransaction homeTransaction = homeFragment.beginTransaction();
//        HomeFragment homeFragment1= new HomeFragment();
//        homeTransaction.add(R.id.nav_host_fragment,homeFragment1);
//        homeTransaction.commit();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.landing_main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        navController.navigate(R.id.map);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Log.d(TAG, "onBackPressed: no! you cant");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.collect:
                startActivity(new Intent(this,RedeemActivity.class));
                break;
            case R.id.promoSelf:
                startActivity(new Intent(this,CheckPromoMerchant.class));
                break;
            case R.id.redeem:
                Intent intent = new Intent(this,RedeemReward.class);
                startActivity(intent);
                break;
            case R.id.scan:
                Intent intentScan = new Intent(this,ScanActivity.class);
                startActivity(intentScan);
                break;
            case R.id.logout_action:
                androidx.appcompat.app.AlertDialog.Builder builderItem = new AlertDialog.Builder(this);

                builderItem.setMessage("Do you want to logout ?");
                builderItem.setCancelable(true);

                builderItem.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
                                if(sharedPreferences.getString("email",null) != null) {
                                    sharedPreferences.edit()
                                            .clear()
                                            .commit();
                                    Intent intent2 = new Intent(LandingMainActivity.this,SplashScreen.class);
                                    startActivity(intent2);
                                }
                            }
                        });
                AlertDialog alert11 = builderItem.create();
                alert11.show();

                Toast.makeText(LandingMainActivity.this,"Registered Complete",Toast.LENGTH_LONG);
                //Clear all UserData Shared Preferences

                break;
        }
        drawer.closeDrawers();
        return true;
    }

    @Override
    public void setsCategoryId(int categoryId) {
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.callApiFiltered(categoryId,LandingMainActivity.this);
    }
    private void updateStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        Call<User> call = Api.getClient().login(sharedPreferences.getString("email"," "), sharedPreferences.getString("password"," "));
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                    String point = sharedPreferences.getString("points","0");
                    Log.d(TAG, "onResponse: Checking point" );
                    if(!point.equals(response.body().getUser().getPoints())){
                        SharedPreferences.Editor editor = getSharedPreferences("UserData",Context.MODE_PRIVATE).edit();
                        editor.putString("points",response.body().getUser().getPoints());
                        editor.commit();
                        Log.d(TAG, "onResponse: different point" );
                        android.app.AlertDialog.Builder builder;
                        builder = new android.app.AlertDialog.Builder(LandingMainActivity.this);
                        View dialog;
                        dialog = LayoutInflater.from(LandingMainActivity.this).inflate(R.layout.custom_dialog_redeem,null,false);
                        TextView name = dialog.findViewById(R.id.mName);
                        name.setText(sharedPreferences.getString("name"," "));
                        TextView description = dialog.findViewById(R.id.info);
                        Button mButton = dialog.findViewById(R.id.buttonOk);
                        description.setText("Your Point's now " + response.body().getUser().getPoints());
                        builder.setView(dialog);
                        android.app.AlertDialog alertDialog = builder.create();

                        mButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.show();

                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
