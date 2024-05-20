package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
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
import com.kickymaulana.com.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    MaterialButton logout;
    MaterialButton bangkitkan_kunci;
    SessionManager sessionManager;
    MaterialButton tandatangani_dokumen;
    MaterialButton periksa_keaslian_dokumen;
    AppCompatTextView nama;
    AppCompatTextView email;
    AppCompatTextView bilangan_e;
    AppCompatTextView bilangan_d;
    AppCompatTextView bilangan_n1;
    AppCompatTextView bilangan_n2;

    RelativeLayout loading;
    MaterialButton daftar_kunci_publik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tanda Tangan Digital");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        nama = (AppCompatTextView) findViewById(R.id.nama);
        email = (AppCompatTextView) findViewById(R.id.email);
        bilangan_e = (AppCompatTextView) findViewById(R.id.bilangan_e);
        bilangan_d = (AppCompatTextView) findViewById(R.id.bilangan_d);
        bilangan_n1 = (AppCompatTextView) findViewById(R.id.bilangan_n1);
        bilangan_n2 = (AppCompatTextView) findViewById(R.id.bilangan_n2);
        sessionManager = new SessionManager(MainActivity.this);
        loading = (RelativeLayout) findViewById(R.id.loading);

        loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(sessionManager.getServer() + "api/beranda")
                .addBodyParameter("email", sessionManager.getUsername())
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
                                Intent intent = new Intent(MainActivity.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else if (response.get("kode").equals("411")) {
                                String[] rule = {"email"};
                                String pesanvalidasi = new PesanValidasi(rule, response).getpesan();
                                loading.setVisibility(View.GONE);
                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage(pesanvalidasi)
                                        .show();
                            } else if (response.get("kode").equals("200")){
                                nama.setText(response.getJSONObject("data").getJSONObject("user").get("nama").toString());
                                email.setText(response.getJSONObject("data").getJSONObject("user").get("email").toString());
                                bilangan_e.setText(response.getJSONObject("data").getJSONObject("user").get("bilangan_e").toString());
                                bilangan_d.setText(response.getJSONObject("data").getJSONObject("user").get("bilangan_d").toString());
                                bilangan_n1.setText(response.getJSONObject("data").getJSONObject("user").get("bilangan_n").toString());
                                bilangan_n2.setText(response.getJSONObject("data").getJSONObject("user").get("bilangan_n").toString());
                                loading.setVisibility(View.GONE);
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

        bangkitkan_kunci = (MaterialButton) findViewById(R.id.bangkitkan_kunci);
        bangkitkan_kunci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BangkitkanKunci.class);
                startActivity(intent);
            }
        });

        logout = (MaterialButton) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        tandatangani_dokumen = (MaterialButton) findViewById(R.id.tandatangani_dokumen);
        tandatangani_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TandaTanganiDokumen.class);
                startActivity(intent);
            }
        });

        periksa_keaslian_dokumen = (MaterialButton) findViewById(R.id.periksa_keaslian_dokumen);
        periksa_keaslian_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PeriksaKeaslianDokumen.class);
                startActivity(intent);
            }
        });

        daftar_kunci_publik = (MaterialButton) findViewById(R.id.daftar_kunci_publik);
        daftar_kunci_publik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DaftarKunciPublik.class);
                startActivity(intent);
            }
        });


    }
}