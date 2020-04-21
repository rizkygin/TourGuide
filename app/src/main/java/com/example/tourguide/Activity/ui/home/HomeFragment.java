package com.example.tourguide.Activity.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.Recommended;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.DataMerchant;
import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.service.Api;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback{

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

    String token;
    private GoogleMap mMap;
    List<Recommended> mList;
Sign
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home,container,false);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = (SupportMapFragment) getActivity()
//                .getSupportFragmentManager().findFragmentById(R.id.map);;
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        recyclerView = root.findViewById(R.id.recyclerView);
        RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(),mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(recyclerViewRecommendedAdapter);

        return root;

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();

        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));
        mList.add(new Recommended("Grand Hotel Swiss Berlin","Jalan farming, No 11,Kec.Seminyek",1));

        SharedPreferences preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);


        token =preferences.getString("token","");


        Call<MerchantIndex> call = Api.getClient().recomendedplace(token);
        call.enqueue(new Callback<MerchantIndex>() {
            @Override
            public void onResponse(Call<MerchantIndex> call, Response<MerchantIndex> response) {
                if(response.body() != null){
                    List<Recommended> list = response.body().getData();
                    for(int i = 0; i < list.size(); i++ ){
                        list.add(new Recommended());
                    }
                }
            }

            @Override
            public void onFailure(Call<MerchantIndex> call, Throwable t) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setTrafficEnabled(true);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney")
        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bag)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}