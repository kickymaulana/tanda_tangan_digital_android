package com.kickymaulana.tandatangandigital;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.kickymaulana.tandatangandigital.adapter.BilanganPrimaAdapter;
import com.kickymaulana.tandatangandigital.model.BilanganPrimaModel;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BilanganPrima extends AppCompatActivity {

    List<BilanganPrimaModel> bilanganPrimaModelList;
    BilanganPrimaAdapter bilanganPrimaAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RelativeLayout loading;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilangan_prima);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bilangan Prima");

        bilanganPrimaModelList = new ArrayList<>();
        bilanganPrimaAdapter = new BilanganPrimaAdapter(BilanganPrima.this, bilanganPrimaModelList);
        linearLayoutManager = new LinearLayoutManager(BilanganPrima.this);
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bilanganPrimaAdapter);
        loading = (RelativeLayout) findViewById(R.id.loading);
        sessionManager = new SessionManager(BilanganPrima.this);

        loading.setVisibility(View.VISIBLE);
        bilanganPrimaModelList.clear();

        for (int i = 1; i <= 100; i++){
            int counter = 0;
            for (int num = i; num >= 1; num--){
                if (i%num == 0){
                    counter = counter + 1;
                }
            }
            if (counter == 2){
                //appended the prime number to the string
                //primeNumbers = primeNumbers + i + " ";
                Log.d("BILANGAN PRIMA", String.valueOf(i));
                String bilangan_prima = String.valueOf(i);
                bilanganPrimaModelList.add(new BilanganPrimaModel(bilangan_prima));
            }
        }
        bilanganPrimaAdapter.notifyDataSetChanged();
        loading.setVisibility(View.GONE);

    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}