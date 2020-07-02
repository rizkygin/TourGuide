package com.example.tourguide.Activity.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.tourguide.Activity.FragamentFilter;
import com.example.tourguide.Activity.GalleryActivity;
import com.example.tourguide.Activity.LandingMainActivity;
import com.example.tourguide.Activity.ListenerFilterRecommended;
import com.example.tourguide.Activity.Merchant;
import com.example.tourguide.Activity.SignInActivity;
import com.example.tourguide.Adapter.Recommended;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.Adapter.TourismAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.DataMerchant;
import com.example.tourguide.model.MerchantIndex;
import com.example.tourguide.model.TourismIndex;
import com.example.tourguide.model.TourismIndexGet;
import com.example.tourguide.service.Api;
import com.example.tourguide.service.FetchURL;
import com.example.tourguide.service.JsonParsers;
import com.example.tourguide.service.TaskLoadedCallback;
import com.example.tourguide.service.UserClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private BottomSheetBehavior bottomSheetBehavior;
    private List<TourismIndexGet> tourism;

    static AdapterListenerFilter listenerFilter;
    ListenerFilterRecommended listener;
    static String categotyFilter = "0";
    static Context mContext;

    TextView recommendedplace;
    private static final String TAG = "LandingMainActivity";
    private HomeViewModel homeViewModel;
    private static RecyclerView recyclerView;
    Integer REQUEST_LOCATION_PERMISSION = 2;

    private Polyline currentPolyline;
    SupportMapFragment mapFragment;

    RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapterFix;
    double currentLat = 0, currentLong = 0;
    static String token;
    Location myLocation=null;
    LatLng lastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    Location mLastLocation;
    static LatLng destination;

    private List<Polyline> polylines=null;
    static String cityComplete;

    private static GoogleMap mMap;
    private GoogleMap map;
    private boolean isReady = false;
    List<Recommended> mList = new ArrayList<>();
    List<TourismIndexGet> mListTourism = new ArrayList<>();
    private  static List<Marker> mMarker = new ArrayList<Marker>();
    double lat = 0.0;
    double lon = 0.0;
    SharedPreferences preferences;
    private static ProgressBar progressBar;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        View bottomSheet = root.findViewById(R.id.bottomSheet);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);
        preferences = getContext().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());



//        RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(), mList);

        cityComplete = getContext().getSharedPreferences("UserData",Context.MODE_PRIVATE).getString("SearchedCity","Search City");

        if(!cityComplete.isEmpty()){
            if(cityComplete.equals("Bali")){
                callApiFilteredByCity(2,getContext());
            }else if (cityComplete.equals("DI Yogyakarta")){
                callApiFilteredByCity(1,getContext());
            }else  {
                callApi();
            }
        }else {
            cityComplete = "Search Here";
            callApi();
        }
        return root;
    }



    private  void filter(String categoryId) {
        ArrayList<Recommended> filter = new ArrayList<>();

        for (int i = 0;i<mList.size();i++){
            if(mList.get(i).getCategory_id() == Integer.parseInt(categoryId)){

                filter.add(mList.get(i));
            }
        }
//        recyclerViewRecommendedAdapter.getResults(filter);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Log.d(TAG, "size mList OnCreate " + mList.size());
        //0
        Log.d(TAG, "isReady " + isReady);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        requestMapPermission();
        Log.d(TAG, "isReady " + isReady);

        googleMap.setTrafficEnabled(true);
        float zoomLevel = 16.0f;


    }

    private void requestMapPermission() {
        Dexter.withActivity(getActivity()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        myLocation=location;
                        lastLocation=new LatLng(location.getLatitude(),location.getLongitude());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("latitude", String.valueOf(lastLocation.latitude));
                        editor.putString("longitude", String.valueOf(lastLocation.longitude));
                        editor.commit();
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                                lastLocation, 16f);
                        currentLat = location.getLatitude();
                        currentLong = location.getLongitude();
                        mMap.animateCamera(cameraUpdate);
                    }
                });

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Toast.makeText(getContext(),"Permission Location Denied",Toast.LENGTH_SHORT ).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

//
    public void callApiFiltered(final int categoryId, final Context mContext){
        if(categoryId != 4 && categoryId!=5) {
            mList = new ArrayList<>();
            mMarker = new ArrayList<Marker>();
            mMap.clear();
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


                        if(recommended.getCategory_id() == categoryId){ //If Merhcant is categoryId
                            mList.add(new Recommended(recommended.getName(), recommended.getAddress(), recommended.getStatus(), recommended.getId(),recommended.getCategory_id()));
                            if(categoryId == 1){

                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(position).title(recommended.getName())
                                        .snippet(String.valueOf(recommended.getId()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_marker))
                                        .title(recommended.getName()));
                                mMarker.add(marker);
                            }else if(categoryId ==2 ){
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(position).title(recommended.getName())
                                        .snippet(String.valueOf(recommended.getId()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker))
                                        .title(recommended.getName()));
                                mMarker.add(marker);
                            }else if(categoryId == 3  ){
                                Marker marker = mMap.addMarker(new MarkerOptions()
                                        .position(position).title(recommended.getName())
                                        .snippet(String.valueOf(recommended.getId()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.resto_marker))
                                        .title(recommended.getName()));
                                mMarker.add(marker);
                            }

                        }

                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
//                        start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                            destination = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
//                        findRoutes(lastLocation,destination);
                            Intent intent = new Intent(mContext, Merchant.class);

                            Bundle bundle = new Bundle();
                            bundle.putInt("idMerchant" , Integer.parseInt(marker.getSnippet()));

                            intent.putExtras(bundle);
                            mContext.startActivity(intent);
                            return false;
                        }
                    });

                    RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(mContext, mList,categoryId);
//                Log.d("testAdapter", "Context Home " + mList.get(1).getId());

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(recyclerViewRecommendedAdapter);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "responses :" + mList.size());


                }

                @Override
                public void onFailure(Call<MerchantIndex> call, Throwable t) {

                }
            });
        }
        else if (categoryId == 4){
            Call<TourismIndex> call = Api.getClient().tourismIndex("Bearer " + token);
            call.enqueue(new Callback<TourismIndex>() {
                @Override
                public void onResponse(Call<TourismIndex> call, Response<TourismIndex> response) {
                    if(response.isSuccessful()){
                        mListTourism = new ArrayList<>();
                        mMarker = new ArrayList<Marker>();
                        mMap.clear();
                        tourism = new ArrayList<>();

                        List<TourismIndexGet> tourisms = response.body().getData();
                        for(TourismIndexGet tourism : tourisms){
                            LatLng position = new LatLng(tourism.getLatitude(), tourism.getLongitude());

                            mListTourism.add(new TourismIndexGet(tourism.getId(),
                                    tourism.getCity_id(),
                                    tourism.getGallery_id(),
                                    tourism.getName(),
                                    tourism.getDescription(),
                                    tourism.getPhoto(),
                                    tourism.getAddress(),
                                    tourism.getLatitude(),
                                    tourism.getLongitude(),
                                    tourism.getCreated_at(),
                                    tourism.getUpdated_at()));
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position).title(tourism.getName())
                                    .snippet(String.valueOf(tourism.getId()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tourism_fix))
                                    .title(tourism.getName()));
                            mMarker.add(marker);
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
//                        start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                                destination = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
//                        findRoutes(lastLocation,destination);


//                                Toast.makeText(getContext(), "Is not Implemented yet", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, GalleryActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putInt("idTourism", Integer.parseInt(marker.getSnippet()));

                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                return false;
                            }
                        });
                        TourismAdapter tourismAdapter = new TourismAdapter(mContext, mListTourism);
//                Log.d("testAdapter", "Context Home " + mList.get(1).getId());

                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(tourismAdapter);
                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<TourismIndex> call, Throwable t) {

                }
            });
        }
        else{
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
//                String responses = response.body().toString();
//                try {
//                    display(responses);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                    List<Recommended> list = response.body().getData();
                    for (Recommended recommended : list) {
                        LatLng position = new LatLng(Double.parseDouble(recommended.getLatitude()), Double.parseDouble(recommended.getLongitude()));

//                    Log.d(TAG, String.valueOf(recommended.getId()));

                        mList.add(new Recommended(recommended.getName(), recommended.getAddress(), recommended.getStatus(), recommended.getId(),recommended.getCategory_id()));
                        if(recommended.getCategory_id() == 1){ //If Merhcant is Shop
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position).title(recommended.getName())
                                    .snippet(String.valueOf(recommended.getId()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_marker))
                                    .title(recommended.getName()));
                            mMarker.add(marker);

                        }else if(recommended.getCategory_id()== 2){//If Hotel is Shop
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position).title(recommended.getName())
                                    .snippet(String.valueOf(recommended.getId()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker))
                                    .title(recommended.getName()));
                            mMarker.add(marker);

                        }else if(recommended.getCategory_id()==3 ){//If Resto is Shop
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position).title(recommended.getName())
                                    .snippet(String.valueOf(recommended.getId()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.resto_marker))
                                    .title(recommended.getName()));
                            mMarker.add(marker);

                        }

                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
//                        start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                            destination = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
//                        findRoutes(lastLocation,destination);
                            Intent intent = new Intent(getContext(), Merchant.class);

                            Bundle bundle = new Bundle();
                            bundle.putInt("idMerchant" , Integer.parseInt(marker.getSnippet()));

                            intent.putExtras(bundle);
                            startActivity(intent);
                            return false;
                        }
                    });
                    RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(mContext, mList,categoryId);
                    Log.d("testAdapter", "Context Home " + mList.get(1).getId());


//                ListenerFilterRecommended listenerFilterRecommended = new ListenerFilterRecommended() {
//                    @Override
//                    public void onCategoryIdChanged(String categoryId) {
//                        recyclerViewRecommendedAdapter.getFilter(categoryId);
//                    }
//                };
                    recyclerViewRecommendedAdapterFix = recyclerViewRecommendedAdapter;
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(recyclerViewRecommendedAdapterFix);
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "responses :" + mList.size());


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
        }

    }
    public void callApi() {
        mMarker = new ArrayList<Marker>();
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
//                String responses = response.body().toString();
//                try {
//                    display(responses);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                List<Recommended> list = response.body().getData();
                for (Recommended recommended : list) {
                    LatLng position = new LatLng(Double.parseDouble(recommended.getLatitude()), Double.parseDouble(recommended.getLongitude()));

//                    Log.d(TAG, String.valueOf(recommended.getId()));

                    mList.add(new Recommended(recommended.getName(), recommended.getAddress(), recommended.getStatus(), recommended.getId(),recommended.getCategory_id()));
                    if(recommended.getCategory_id() == 1){ //If Merhcant is Shop
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(position).title(recommended.getName())
                                .snippet(String.valueOf(recommended.getId()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.shop_marker))
                                .title(recommended.getName()));
                        mMarker.add(marker);

                    }else if(recommended.getCategory_id()== 2){//If Hotel is Shop
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(position).title(recommended.getName())
                                .snippet(String.valueOf(recommended.getId()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.hotel_marker))
                                .title(recommended.getName()));
                        mMarker.add(marker);

                    }else if(recommended.getCategory_id()==3 ){//If Resto is Shop
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(position).title(recommended.getName())
                                .snippet(String.valueOf(recommended.getId()))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.resto_marker))
                                .title(recommended.getName()));
                        mMarker.add(marker);

                    }

                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
//                        start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                        destination = new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
//                        findRoutes(lastLocation,destination);
                        Intent intent = new Intent(getContext(), Merchant.class);

                        Bundle bundle = new Bundle();
                        bundle.putInt("idMerchant" , Integer.parseInt(marker.getSnippet()));

                        intent.putExtras(bundle);
                        startActivity(intent);
                        return false;
                    }
                });
                RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(), mList,5);
                Log.d("testAdapter", "Context Home " + mList.get(1).getId());


//                ListenerFilterRecommended listenerFilterRecommended = new ListenerFilterRecommended() {
//                    @Override
//                    public void onCategoryIdChanged(String categoryId) {
//                        recyclerViewRecommendedAdapter.getFilter(categoryId);
//                    }
//                };
                recyclerViewRecommendedAdapterFix = recyclerViewRecommendedAdapter;
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerViewRecommendedAdapterFix);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "responses :" + mList.size());


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

    public void callApiFilteredByCity(int city, final Context mContext) {
        if(city != 3){
            Call<TourismIndex> call = Api.getClient().tourismIndexCity("Bearer " + token,city);
            call.enqueue(new Callback<TourismIndex>() {
                @Override
                public void onResponse(Call<TourismIndex> call, Response<TourismIndex> response) {
                    if(response.isSuccessful()){
                        mListTourism = new ArrayList<>();
                        mMarker = new ArrayList<Marker>();
                        mMap.clear();
                        tourism = new ArrayList<>();

                        List<TourismIndexGet> tourisms = response.body().getData();
                        for(TourismIndexGet tourism : tourisms){
                            LatLng position = new LatLng(tourism.getLatitude(), tourism.getLongitude());

                            mListTourism.add(new TourismIndexGet(tourism.getId(),
                                    tourism.getCity_id(),
                                    tourism.getGallery_id(),
                                    tourism.getName(),
                                    tourism.getDescription(),
                                    tourism.getPhoto(),
                                    tourism.getAddress(),
                                    tourism.getLatitude(),
                                    tourism.getLongitude(),
                                    tourism.getCreated_at(),
                                    tourism.getUpdated_at()));
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(position).title(tourism.getName())
                                    .snippet(String.valueOf(tourism.getId()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.tourism_fix))
                                    .title(tourism.getName()));
                            mMarker.add(marker);
                        }
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
//                        start=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                                destination = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
//                        findRoutes(lastLocation,destination);


//                                Toast.makeText(getContext(), "Is not Implemented yet", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(mContext, GalleryActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putInt("idTourism", Integer.parseInt(marker.getSnippet()));

                                intent.putExtras(bundle);
                                mContext.startActivity(intent);
                                return false;
                            }
                        });
                        TourismAdapter tourismAdapter = new TourismAdapter(mContext, mListTourism);
//                Log.d("testAdapter", "Context Home " + mList.get(1).getId());

                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(tourismAdapter);
                        progressBar.setVisibility(View.GONE);

                    }
                }

                @Override
                public void onFailure(Call<TourismIndex> call, Throwable t) {

                }
            });
        }
       else{
           callApi();
        }
    }

//    private void display(String responses) throws JSONException{
//        Log.d(TAG, "display: success through here");
//        JSONObject object = new JSONObject(responses);
//        if(object.optBoolean("success")){
//            progressBar.setVisibility(View.GONE);
//            JSONArray data  = object.getJSONArray("data");
//            for (int i = 0; i < data.length(); i++){
//                JSONObject obj = data.getJSONObject(i);
//                Log.d(TAG, "display: "+ obj);
//                mList.add(new Recommended(obj.getString("name"),obj.getString("address"),String.valueOf(obj.getInt("status")),obj.getInt("id")));
//            }
//            RecyclerViewRecommendedAdapter recyclerViewRecommendedAdapter = new RecyclerViewRecommendedAdapter(getContext(), mList);
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//            recyclerView.setAdapter(recyclerViewRecommendedAdapter);
//            progressBar.setVisibility(View.GONE);
//
//        }
//    }


    private class Placetask extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {
//            initateData
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;

        }

        @Override
        protected void onPostExecute(String s) {
            //Execute parser task

            new ParserTask().execute(s);
        }
    }

    private String downloadUrl(String string) throws IOException{
        //Initalize url
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //Connect
        connection.connect();
        //initalize input stream
        InputStream stream = connection.getInputStream();
        //Initialize buffer reader
        BufferedReader reader = new BufferedReader((new InputStreamReader(stream)));

        //Initialize string builder
        StringBuilder builder= new StringBuilder();
        //Initialize string variable
        String line = "";
        //use while loop
        while ((line = reader.readLine())!= null){
            //Append Line
            builder.append(line);
        }
        //getappend data
        String data = builder.toString();
        //Close Reader
        reader.close();
        return data;

    }

    private class ParserTask extends  AsyncTask<String,Integer,List<HashMap<String,String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            //Create json parser class
            JsonParsers jsonParsers = new JsonParsers();
            List<HashMap<String,String>> mapList = null;
            JSONObject object = null;
            try {
                //Initialize Hash Map list
                if(strings[0] != null){
                    object = new JSONObject(strings[0]);
                }
                 //parse json object
                mapList= jsonParsers.parseResult(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Return map List
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();

            for(int i=0; i<hashMaps.size();i++){
                HashMap<String,String> hashMapList = hashMaps.get(i);
                //Get Latitdue
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");

                //Contacnt Latitdue and Longitude

                LatLng latLng = new LatLng(lat,lng);

                MarkerOptions options = new MarkerOptions();
                //sets positions
                options.position(latLng)
                .title(name);

                mMap.addMarker(options);
            }
        }
    }

}