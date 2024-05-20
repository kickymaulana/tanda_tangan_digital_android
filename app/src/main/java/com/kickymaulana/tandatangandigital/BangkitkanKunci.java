package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.tandatangandigital.penolong.KunciDeskripsiHelper;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

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

    MaterialButton hitung_nilai_d;


    BigInteger e_big;
    BigInteger d_big;
    BigInteger f_n_big;
    BigInteger p_big;
    BigInteger q_big;
    BigInteger n_big;

    AppCompatTextView nilai_d;
    AppCompatTextView hasil_e, hasil_d, hasil_n1, hasil_n2;

    MaterialButton simpan;
    RelativeLayout loading;
    SessionManager sessionManager;


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

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Bangkitkan Kunci");

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
                if (pilih_bilangan_prima_p.getText().equals("pilih bilangan") || pilih_bilangan_prima_q.getText().equals("pilih bilangan")){
                    Toast.makeText(BangkitkanKunci.this, "Silahkan pilh terlebih dahulu bilangan", Toast.LENGTH_SHORT).show();
                } else {
                    p = Integer.valueOf(bilangan_prima_p_s);
                    q = Integer.valueOf(bilangan_prima_q_s);
                    rumus_p.setText("p = " + p + ", q = " + q);
                    n = p * q;
                    rumus_n.setText("n = p x q = " + p + " x " + q + " = " + n);
                    fn = (p-1) * (q-1);
                    rumus_fn.setText("f(n) = (" + p + " - 1) x (" + q + " - 1) = " + fn);
                    pilih_bilangan_e.setText("Pilih satu bilangan(e) yang relatif prima terhadap nilai f(n) = " + fn);

                }


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
        nilai_d = (AppCompatTextView) findViewById(R.id.nilai_d);
        hitung_nilai_d = (MaterialButton) findViewById(R.id.hitung_nilai_d);
        hasil_e = (AppCompatTextView) findViewById(R.id.hasil_e);
        hasil_d = (AppCompatTextView) findViewById(R.id.hasil_d);
        hasil_n1 = (AppCompatTextView) findViewById(R.id.hasil_n1);
        hasil_n2 = (AppCompatTextView) findViewById(R.id.hasil_n2);
        hitung_nilai_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pilih_bilangan_relatif_prima_e.getText().equals("pilih bilangan")){
                    Toast.makeText(BangkitkanKunci.this, "pilih terlebih dahulu bilangan yang relatif prima", Toast.LENGTH_SHORT).show();
                } else {
                    e_big = new BigInteger(bilangan_relatif_prima_e_s);
                    p_big = new BigInteger(String.valueOf(p));
                    q_big = new BigInteger(String.valueOf(q));
                    n_big = p_big.multiply(q_big);
                    f_n_big = p_big.subtract(new BigInteger("1")).multiply(q_big.subtract(new BigInteger("1")));
                    KunciDeskripsiHelper kunciDeskripsi = new KunciDeskripsiHelper();
                    d_big = kunciDeskripsi.jalankan(f_n_big, e_big);
                    nilai_d.setText(String.valueOf(d_big));

                    hasil_e.setText(String.valueOf(e_big));
                    hasil_d.setText(String.valueOf(d_big));
                    hasil_n1.setText(String.valueOf(n_big));
                    hasil_n2.setText(String.valueOf(n_big));

                }

            }
        });

        simpan = (MaterialButton) findViewById(R.id.simpan);
        sessionManager = new SessionManager(BangkitkanKunci.this);
        loading = (RelativeLayout) findViewById(R.id.loading);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                AndroidNetworking.post(sessionManager.getServer() + "api/update-public-and-private-key")
                        .addBodyParameter("email", sessionManager.getUsername())
                        .addBodyParameter("bilangan_e", hasil_e.getText().toString())
                        .addBodyParameter("bilangan_d", hasil_d.getText().toString())
                        .addBodyParameter("bilangan_n", hasil_n1.getText().toString())
                        .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("RESPONSE", response.toString());
                                    if (response.get("kode").equals("401")) {
                                        loading.setVisibility(View.GONE);
                                        sessionManager.logout();
                                        Intent intent = new Intent(BangkitkanKunci.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (response.get("kode").equals("411")) {
                                        String[] rule = {"email", "bilangan_e", "bilangan_d", "bilangan_n"};
                                        String pesanvalidasi = new PesanValidasi(rule, response).getpesan();
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(BangkitkanKunci.this)
                                                .setMessage(pesanvalidasi)
                                                .show();
                                    } else if (response.get("kode").equals("200")){
                                        loading.setVisibility(View.GONE);
                                        Toast.makeText(BangkitkanKunci.this, response.get("pesan").toString(), Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                loading.setVisibility(View.GONE);
                            }

                        });

            }
        });

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}