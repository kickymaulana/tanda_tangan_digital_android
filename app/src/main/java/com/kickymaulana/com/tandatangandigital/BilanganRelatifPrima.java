package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
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
import com.kickymaulana.com.tandatangandigital.adapter.BilanganPrimaAdapter;
import com.kickymaulana.com.tandatangandigital.adapter.BilanganRelatifPrimaAdapter;
import com.kickymaulana.com.tandatangandigital.model.BilanganPrimaModel;
import com.kickymaulana.com.tandatangandigital.model.BilanganRelatifPrimaModel;
import com.kickymaulana.com.tandatangandigital.penolong.BilanganRelatifPrimaHelper;
import com.kickymaulana.com.tandatangandigital.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class BilanganRelatifPrima extends AppCompatActivity {

    List<BilanganRelatifPrimaModel> bilanganRelatifPrimaModelList;
    BilanganRelatifPrimaAdapter bilanganRelatifPrimaAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RelativeLayout loading;
    SessionManager sessionManager;

    int fn;
    String fn_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bilangan_relatif_prima);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        fn_s = intent.getStringExtra("fn");
        fn = Integer.parseInt(fn_s);


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bilangan Relatif Prima");

        bilanganRelatifPrimaModelList = new ArrayList<>();
        bilanganRelatifPrimaAdapter = new BilanganRelatifPrimaAdapter(BilanganRelatifPrima.this, bilanganRelatifPrimaModelList);
        linearLayoutManager = new LinearLayoutManager(BilanganRelatifPrima.this);
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(bilanganRelatifPrimaAdapter);
        loading = (RelativeLayout) findViewById(R.id.loading);
        sessionManager = new SessionManager(BilanganRelatifPrima.this);

        loading.setVisibility(View.VISIBLE);
        bilanganRelatifPrimaModelList.clear();

        BilanganRelatifPrimaHelper bilanganRelatifPrimaHelper = new BilanganRelatifPrimaHelper();

        for (int i = 1; i < fn; i++) {
            if (bilanganRelatifPrimaHelper.isRelatifPrima(fn, i)) {
                String bilangan_relatif_prima = String.valueOf(i);
                bilanganRelatifPrimaModelList.add(new BilanganRelatifPrimaModel(bilangan_relatif_prima));
            }
        }


        bilanganRelatifPrimaAdapter.notifyDataSetChanged();
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