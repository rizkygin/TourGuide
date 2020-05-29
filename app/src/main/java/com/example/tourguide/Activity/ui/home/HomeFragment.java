package com.example.tourguide.Activity.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.Activity.SignInActivity;
import com.example.tourguide.Adapter.Recommended;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.DataMerchant;
import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.UserClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "LandingMainActivity";
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    Integer REQUEST_LOCATION_PERMISSION = 2;


    String token;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;

    private GoogleMap mMap;
    private boolean isReady = false;
    List<Recommended> mList = new ArrayList<>();
    private final List<Marker> mMarker = new ArrayList<Marker>();
    double lat = 0.0;
    double lon = 0.0;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity()
//                .getSupportFragmentManager().findFragmentById(R.id.map);;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

//        RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(), mList);
        callApi();
        return root;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        Log.d(TAG, "size mList OnCreate " + mList.size());
        //0
        Log.d(TAG, "isReady " + isReady);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "isReady " + isReady);
        mFusedLocationClient.getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            mLastLocation = location;


                            lat = mLastLocation.getLatitude();
                            lon = mLastLocation.getLongitude();

                        } else {
                            Toast.makeText(getContext(), "No location Found", Toast.LENGTH_LONG).show();
                        }

                    }

                }
        );

        Latlo latlo = new Latlo(lat,lon);

        googleMap.setTrafficEnabled(true);
        float zoomLevel = 16.0f;
        mMap = googleMap;


        LatLng sydney = new LatLng(latlo.getLat(),latlo.getLo());

        mMap.addMarker(new MarkerOptions()
                .title("Your Position")
                .position(sydney)
                .icon(BitmapDescriptorFactory.defaultMarker(150)));
//            addMarkersToMap();
//        mList.add(new Recommended("hello","there","0"));
//        Log.d(TAG, String.valueOf(mList.size()));

        // Add a marker in Sydney and move the camera
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
//                .icon(null));


//        Log.d(TAG,"Name " +mList.get(30).getName()+ " "+ mList.size());
//        mMap.addMarker(new MarkerOptions()
//                .position(sydney)
//                        .title(mList.get(1).getName())
//                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bag)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, zoomLevel));


    }

//    private void addMarkersToMap() {
//        if(mList.size() != 0 ){
//            return;
//        }
//        else {
//            callApi();
//            addMarkersToMap();
//        }
//    }

    //    private void addMarkersToMap() {
//        if(mList.size() != 0){
//            for (int i = 0; i < mList.size(); i++) {
//                mMap.addMarker(new MarkerOptions()
//                        .position(new LatLng(Double.parseDouble(mList.get(i).getLatitude()),Double.parseDouble(mList.get(i).getLongitude())))
//                        .title(mList.get(i).getName())
//                        .icon(BitmapDescriptorFactory.defaultMarker(R.drawable.shop_marker)));
//
//            }
//
//        }
//        else if (mList.size() == 0){
//            callApi();
////            Log.d(TAG,"berulang Kali ini cuk");
//            Log.d(TAG,"Size : " + mList.size());
////            addMarkersToMap();
//        }
//    }
    private void callApi() {
        Call<MerchantIndex> call = Api.getClient().recomendedplace("Bearer " + token);
        call.enqueue(new Callback<MerchantIndex>() {
            @Override
            public void onResponse(Call<MerchantIndex> call, Response<MerchantIndex> response) {
                progressBar.setVisibility(View.VISIBLE);

                Log.d(TAG, response.message());
                if (!response.isSuccessful()) {
                    Log.d(TAG, "Code :" + response.code());
                    return;
                }
                List<Recommended> list = response.body().getData();
                for (Recommended recommended : list) {
                    LatLng position = new LatLng(Double.parseDouble(recommended.getLatitude()), Double.parseDouble(recommended.getLongitude()));

                    Log.d(TAG, String.valueOf(recommended.getId()));
                    mList.add(new Recommended(recommended.getName(), recommended.getAddress(), recommended.getStatus(), recommended.getId()));
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(position).title(recommended.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_marker))
                            .title(recommended.getName()));
                    mMarker.add(marker);

                }
                RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(), mList);
                Log.d("testAdapter", "Context Home " + mList.get(1).getId());

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerViewRecommendedAdapter);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "responses :" + mList.size());
                //30

            }

            @Override
            public void onFailure(Call<MerchantIndex> call, Throwable t) {
                progressBar.setVisibility(View.VISIBLE);

                String Message = t.getMessage();
                Log.d(TAG + "f", Message);
//                mList.add(new Recommended("Grand Hotel Swiss Berlin", "Jalan farming, No 11,Kec.Seminyek", "1"));
//                Toast.makeText(SignInActivity.this,"Sorry Try Again" + emailtype + "/ " + "pass " + Message,Toast.LENGTH_LONG ).show();
////                Log.d("response", t.getStackTrace().toString());
                if (t instanceof SocketTimeoutException) {
                    Toast.makeText(getActivity(), "Connection time out  " + Message, Toast.LENGTH_LONG).show();
                } else if (t instanceof IOException) {
                    Toast.makeText(getActivity(), "Time Out  " + Message, Toast.LENGTH_LONG).show();
                } else {
                    //Call was cancelled by user
                    if (call.isCanceled()) {
                        System.out.println("Call was cancelled forcefully");
                    } else {
                        //Generic error handling
                        System.out.println("Network Error :: " + t.getLocalizedMessage());
                    }
                }
            }
        });

        isReady = true;
    }


    private class Latlo {
        double lat;
        double lo;

        public Latlo() {
        }

        public Latlo(double lat, double lo) {
            this.lat = lat;
            this.lo = lo;
        }

        public double getLat() {
            return lat;
        }

        public double getLo() {
            return lo;
        }
    }
}