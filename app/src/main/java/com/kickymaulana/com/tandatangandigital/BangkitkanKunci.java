package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.kickymaulana.com.tandatangandigital.penolong.BilanganPrimaHelper;

public class BangkitkanKunci extends AppCompatActivity {

    MaterialButton hitung_nilai_fn;
    AppCompatTextView pilih_bilangan_prima_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bangkitkan_kunci);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hitung_nilai_fn = (MaterialButton) findViewById(R.id.hitung_nilai_fn);
        hitung_nilai_fn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BilanganPrimaHelper bilanganPrimaHelper = new BilanganPrimaHelper();
                Log.d("BILANGAN PRIMA", bilanganPrimaHelper.tampilkan());
            }
        });

        pilih_bilangan_prima_p = (AppCompatTextView) findViewById(R.id.pilih_bilangan_prima_p);
        pilih_bilangan_prima_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangkitkanKunci.this, BilanganPrima.class);
                startActivity(intent);
            }
        });
    }
}