package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.RecyclerCity;
import com.example.tourguide.Adapter.RecyclerViewRecommendedAdapter;
import com.example.tourguide.R;
import com.google.android.material.button.MaterialButton;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SearchCityActivity extends AppCompatActivity {

    private static final String TAG = "SearchCityActivity";

    RecyclerCity recyclerCity;

    MaterialButton mBackButton;
    EditText mActvCity;
    RecyclerView recyclerView;
    List<String> mList=  new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);

        recyclerView = findViewById(R.id.rcCitySC);
        mActvCity = findViewById(R.id.actvCity);
        mBackButton = findViewById(R.id.btnBackSC);

        mActvCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        addCity();


        recyclerCity = new RecyclerCity(this, mList);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(SearchCityActivity.this));
        recyclerView.setAdapter(recyclerCity);

        Log.d(TAG, "onCreate: " + mList.size());
    }

    private void addCity() {
        mList.add("Ambon");
        mList.add("Balikpapan");
        mList.add("BANDUNG");
        mList.add("Banda Aceh");
        mList.add("Bandar Lampung");
        mList.add("Bandung");
        mList.add("Banjar");
        mList.add("Banjarbaru");
        mList.add("Banjarmasin");
        mList.add("Batam");
        mList.add("Batu");
        mList.add("Bau-Bau");
        mList.add("Bekasi");
        mList.add("Bima");
        mList.add("Binjai");
        mList.add("Blitar");
        mList.add("Bogor");
        mList.add("Bukittinggi");
        mList.add("Cilegon");
        mList.add("Cimahi");
        mList.add("Cirebon");
        mList.add("Depok");
        mList.add("Dumai");
        mList.add("Gorontalo");
        mList.add("Gunungsitoli");
        mList.add("Jakarta Barat");
        mList.add("Jakarta Pusat");
        mList.add("Jakarta Selatan");
        mList.add("Jakarta Utara");
        mList.add("Jambi");
        mList.add("Jayapura");
        mList.add("Kediri");
        mList.add("Kendari");
        mList.add("Kupang");
        mList.add("Langsa");
        mList.add("Lhokseumawe");
        mList.add("Lubuklinggau");
        mList.add("Madiun");
        mList.add("Magelang");
        mList.add("Makassar");
        mList.add("Malang");
        mList.add("Manado");
        mList.add("Metro");
        mList.add("Mojokerto");
        mList.add("Padang");
        mList.add("Padangpanjang");
        mList.add("Padangsidempuan");
        mList.add("Pagar Alam");
        mList.add("Palangkaraya");
        mList.add("Pangkalan Bun");
        mList.add("Palembang");
        mList.add("Palopo");
        mList.add("Palu");
        mList.add("Pangkal Pinang");
        mList.add("Parepare");
        mList.add("Pariaman");
        mList.add("Pasuruan");
        mList.add("Payakumbuh");
        mList.add("Pekalongan");
        mList.add("Pekanbaru");
        mList.add("Pematangsiantar");
        mList.add("Pontianak");
        mList.add("Prabumulih");
        mList.add("Probolinggo");
        mList.add("Sabang");
        mList.add("Salatiga");
        mList.add("Samarinda");
        mList.add("Sawahlunto");
        mList.add("Semarang");
        mList.add("Serang");
        mList.add("Sibolga");
        mList.add("Singkawang");
        mList.add("Solok");
        mList.add("Sorong");
        mList.add("Sukabumi");
        mList.add("Sungai Penuh");
        mList.add("Surabaya");
        mList.add("Surakarta");
        mList.add("Tangerang Selatan");
        mList.add("Tangerang");
        mList.add("Tanjungbalai");
        mList.add("Tanjung Pinang");
        mList.add("Tarakan");
        mList.add("Tasikmalaya");
        mList.add("Tebing Tinggi");
        mList.add("Tegal");
        mList.add("Ternate");
        mList.add("Tidore Kepulauan");
        mList.add("Tomohon");
        mList.add("Tual");
        mList.add("Yogyakarta");
    }

    private void filter(String text){
        ArrayList<String> filteredList = new ArrayList<>();

        for (String city : mList ){
            if(city.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(city);
            }
        }


        recyclerCity.filterList(filteredList);
    }
}
