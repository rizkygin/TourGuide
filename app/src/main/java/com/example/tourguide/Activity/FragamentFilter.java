package com.example.tourguide.Activity;

import android.content.Context;
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
    MaterialButton mWisata,mShop,mResto,mHotel;
    private static final String TAG ="FragmentFilter";
    ListenerFilterRecommended listener;
    String categoryId= "4";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.filter_fragment, container, false);

        mWisata = view.findViewById(R.id.btnWisata);
        mShop = view.findViewById(R.id.btnSouvernir);
        mResto = view.findViewById(R.id.btnFood);
        mHotel = view.findViewById(R.id.btnHotel);


        mWisata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(4);
                mWisata.setClickable(false);
            }
        });
        mResto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(3);

            }
        });
        mHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(2);

            }
        });
        mShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.setsCategoryId(1);

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
