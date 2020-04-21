package com.example.tourguide.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourguide.Adapter.RecyclerViewVoucherAdapter;
import com.example.tourguide.Adapter.Voucher;
import com.example.tourguide.R;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class RedeemActivity extends AppCompatActivity {

    List<Voucher> mList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redeem);



        recyclerView = findViewById(R.id.RecycleListVoucher);

        mList= new ArrayList<>();
        mList.add(new Voucher("Auto Leader",15000,true));
        mList.add(new Voucher("Rumah Makan Kita",35000,true));
        mList.add(new Voucher("Warung Santai ",23000,true));
        mList.add(new Voucher("Pak Tarno",25000,true));
        mList.add(new Voucher("Swadihap",15000,true));
        mList.add(new Voucher("Auto Leader 2",10000,true));
        mList.add(new Voucher("Bantal Ban ",15000,true));
        mList.add(new Voucher("Tidur Nyenyak Hotel",15000,true));
        mList.add(new Voucher("Best Cover Hotel",15000,true));
        mList.add(new Voucher("Safety Hotel",25000,true));
        mList.add(new Voucher("Kikuk",35000,true));


        RecyclerViewVoucherAdapter recyclerViewVoucherAdapter = new RecyclerViewVoucherAdapter(this,mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewVoucherAdapter);
    }
}
