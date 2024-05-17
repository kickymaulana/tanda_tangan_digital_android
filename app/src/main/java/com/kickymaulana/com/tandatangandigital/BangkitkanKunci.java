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

    MaterialButton hitung_nilai_fn;
    AppCompatTextView pilih_bilangan_prima_p;
    ActivityResultLauncher<Intent> bilangan_prima_p_launcher;
    String bilangan_prima_p_s;

    AppCompatTextView pilih_bilangan_prima_q;
    ActivityResultLauncher<Intent> bilangan_prima_q_launcher;
    String bilangan_prima_q_s;


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
    }
}