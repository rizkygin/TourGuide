package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.tourguide.Adapter.GalleryAdapter;
import com.example.tourguide.Adapter.TourismAdapter;
import com.example.tourguide.R;
import com.example.tourguide.model.GalleryModel;
import com.example.tourguide.model.GalleryResponse;
import com.example.tourguide.service.Api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GalleryActivity extends AppCompatActivity {
    private static final String TAG = "GalleryActivity" ;
    List<GalleryModel> mList;
    TextView mTitle;
    TextView mDescription;
    TextView mLocation;
    Button mBack;
    Button showGoogle;
    RecyclerView recyclerView;
    String token;
    Double latitude;
    Double longitude;
    int path;
    ImageView little_touch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        mTitle = findViewById(R.id.title);
        mDescription = findViewById(R.id.tvDescRelative);
        mLocation = findViewById(R.id.tvAddressRelative);
        mBack = findViewById(R.id.backButton);
        showGoogle = findViewById(R.id.shoGoogle);
        recyclerView = findViewById(R.id.gGallery);
        little_touch = findViewById(R.id.labelToFront);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GalleryActivity.this, LandingMainActivity.class));
            }
        });

        token = getSharedPreferences("UserData", Context.MODE_PRIVATE).getString("token", " ");
        path = getIntent().getIntExtra("idTourism", 0);
        mTitle.setText(getIntent().getStringExtra("nameTourism"));
        mLocation.setText(getIntent().getStringExtra("location"));
        mDescription.setText(getIntent().getStringExtra("description"));
        callApi();
        Log.d(TAG, "callApi: " + path);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        showGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri gmmIntentUri = Uri.parse("geo:" + latitude +","+longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

    }

    private void callApi() {

        if (path != 0) {
            mList = new ArrayList<>();
            Call<GalleryResponse> called = Api.getClient().tourismIndexing("Bearer " + token, path);
            called.enqueue(new Callback<GalleryResponse>() {
                @Override
                public void onResponse(Call<GalleryResponse> call, Response<GalleryResponse> response) {
                    if (response.isSuccessful()) {
                        List<GalleryModel> tourism = response.body().getData();
                        if (tourism.size() == 0) {
                            mList.add(new GalleryModel(0,
                                    0,
                                    "No Description For this",
                                    " ",
                                    " ",
                                    " "));
                        }
                        for (GalleryModel tour : tourism) {
                            if (tour.getDescription() == null) {
                                mList.add(new GalleryModel(tour.getId(),
                                        tour.getTourism_id(),
                                        "This is the place that our recommended for you to visit , or just hang out to enjoy the view.Come here with your friend will make fun more fun :) Admin Travel Guide Love you <3",
                                        tour.getPhoto(),
                                        tour.getCreated_at(),
                                        tour.getUpdated_at()));
                                Log.d(TAG, "onResponse: "+ tour.getPhoto());
                            } else {
                                mList.add(new GalleryModel(tour.getId(),
                                        tour.getTourism_id(),
                                        tour.getDescription(),
                                        tour.getPhoto(),
                                        tour.getCreated_at(),
                                        tour.getUpdated_at()));
                            }

                        }
                        GalleryAdapter tourismAdapter = new GalleryAdapter(GalleryActivity.this, mList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this, RecyclerView.HORIZONTAL, false));
                        recyclerView.setAdapter(tourismAdapter);

                    }
                }

                @Override
                public void onFailure(Call<GalleryResponse> call, Throwable t) {

                }
            });

        }
    }

}
