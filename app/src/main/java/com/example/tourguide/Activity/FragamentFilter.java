package com.example.tourguide.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourguide.Activity.ui.home.HomeFragment;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.R;
import com.google.android.material.button.MaterialButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragamentFilter extends Fragment {
    MaterialButton mWisata,mShop,mResto,mHotel,mRecommend;
    private static final String TAG ="FragmentFilter";
    ListenerFilterRecommended listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.filter_fragment, container, false);

        mWisata = view.findViewById(R.id.btnWisata);
        mShop = view.findViewById(R.id.btnSouvernir);
        mResto = view.findViewById(R.id.btnFood);
        mRecommend = view.findViewById(R.id.btnRecommend);
        mHotel = view.findViewById(R.id.btnHotel);
        final Drawable noClick;
        noClick = getResources().getDrawable(R.drawable.no_clicked);

//        mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));
        mRecommend.setClickable(false);
        mRecommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(5);
                mRecommend.setClickable(false);
                mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));

                mShop.setClickable(true);
                mResto.setClickable(true);
                mHotel.setClickable(true);
                mWisata.setClickable(true);

                mShop.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mResto.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mHotel.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mWisata.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));

            }
        });
        mWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.setsCategoryId(4);

                mWisata.setClickable(false);
                mWisata.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));
                mShop.setClickable(true);
                mResto.setClickable(true);
                mHotel.setClickable(true);
                mRecommend.setClickable(true);
                mShop.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mResto.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mHotel.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));



            }
        });
        mResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(3);


                mResto.setClickable(false);
                mResto.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));
                mWisata.setClickable(true);
                mShop.setClickable(true);
                mHotel.setClickable(true);
                mRecommend.setClickable(true);
                mWisata.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mShop.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mHotel.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));


            }
        });
        mHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(2);

                mHotel.setClickable(false);
                mHotel.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));
                mResto.setClickable(true);
                mWisata.setClickable(true);
                mShop.setClickable(true);
                mRecommend.setClickable(true);
                mResto.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mWisata.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mShop.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));


            }
        });
        mShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(1);

                mShop.setClickable(false);
                mShop.setBackgroundColor(getResources().getColor(R.color.my_app_button_clicked));
                mHotel.setClickable(true);
                mResto.setClickable(true);
                mWisata.setClickable(true);
                mRecommend.setClickable(true);
                mHotel.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mResto.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mWisata.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));
                mRecommend.setBackgroundColor(getResources().getColor(R.color.my_app_background_color));

            }
        });
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof ListenerFilterRecommended){
            listener = (ListenerFilterRecommended) context;
        }else{
            throw  new RuntimeException(context.toString()
                    + " must implement ListenerFilterRecommended");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
