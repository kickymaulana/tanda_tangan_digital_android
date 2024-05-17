package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.kickymaulana.com.tandatangandigital.penolong.BilanganPrimaHelper;

public class BangkitkanKunci extends AppCompatActivity {

    AppCompatTextView pilih_bilangan_prima_p;
    ActivityResultLauncher<Intent> bilangan_prima_p_launcher;
    String bilangan_prima_p_s;

    AppCompatTextView pilih_bilangan_prima_q;
    ActivityResultLauncher<Intent> bilangan_prima_q_launcher;
    String bilangan_prima_q_s;
    String bilangan_relatif_prima_e_s;

    MaterialButton lengkapi_rumus;
    Integer p, q, n, fn;
    AppCompatTextView rumus_p;
    AppCompatTextView rumus_n;
    AppCompatTextView rumus_fn;

    AppCompatTextView pilih_bilangan_e;
    ActivityResultLauncher<Intent> pilih_bilangan_relatif_prima_e_launcher;
    AppCompatTextView pilih_bilangan_relatif_prima_e;


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

        pilih_bilangan_prima_p = (AppCompatTextView) findViewById(R.id.pilih_bilangan_prima_p);

        bilangan_prima_p_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    bilangan_prima_p_s = result.getData().getStringExtra("bilangan_prima");
                    pilih_bilangan_prima_p.setText(bilangan_prima_p_s);
                }

            }
        });
        pilih_bilangan_prima_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangkitkanKunci.this, BilanganPrima.class);
                bilangan_prima_p_launcher.launch(intent);
            }
        });

        pilih_bilangan_prima_q = (AppCompatTextView) findViewById(R.id.pilih_bilangan_prima_q);

        bilangan_prima_q_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    bilangan_prima_q_s = result.getData().getStringExtra("bilangan_prima");
                    pilih_bilangan_prima_q.setText(bilangan_prima_q_s);
                }

            }
        });
        pilih_bilangan_prima_q.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangkitkanKunci.this, BilanganPrima.class);
                bilangan_prima_q_launcher.launch(intent);
            }
        });

        lengkapi_rumus = (MaterialButton) findViewById(R.id.lengkapi_rumus);
        rumus_p = (AppCompatTextView) findViewById(R.id.rumus_p);
        rumus_n = (AppCompatTextView) findViewById(R.id.rumus_n);
        rumus_fn = (AppCompatTextView) findViewById(R.id.rumus_fn);

        pilih_bilangan_e = (AppCompatTextView) findViewById(R.id.pilih_bilangan_e);
        lengkapi_rumus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = Integer.valueOf(bilangan_prima_p_s);
                q = Integer.valueOf(bilangan_prima_q_s);
                rumus_p.setText("p = " + p + ", q = " + q);
                n = p * q;
                rumus_n.setText("n = p x q = " + p + " x " + q + " = " + n);
                fn = (p-1) * (q-1);
                rumus_fn.setText("f(n) = (" + p + " - 1) x (" + q + " - 1) = " + fn);
                pilih_bilangan_e.setText("Pilih satu bilangan(e) yang relatif prima terhadap nilai f(n) = " + fn);

            }
        });

        pilih_bilangan_relatif_prima_e_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    bilangan_relatif_prima_e_s = result.getData().getStringExtra("bilangan_relatif_prima");
                    pilih_bilangan_relatif_prima_e.setText(bilangan_relatif_prima_e_s);
                }

            }
        });

        pilih_bilangan_relatif_prima_e = (AppCompatTextView) findViewById(R.id.pilih_bilangan_relatif_prima_e);
        pilih_bilangan_relatif_prima_e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BangkitkanKunci.this, BilanganRelatifPrima.class);
                String fn_s = String.valueOf(fn);
                intent.putExtra("fn", fn_s);
                pilih_bilangan_relatif_prima_e_launcher.launch(intent);

            }
        });

    }
}