package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Login extends AppCompatActivity {

    AppCompatEditText email;
    AppCompatEditText password;
    MaterialButton login;
    RelativeLayout loading;
    SessionManager sessionManager;
    AppCompatTextView periksa_keaslian_dokumen;
    AppCompatTextView register;

    AppCompatTextView teks_kebijakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        email = (AppCompatEditText) findViewById(R.id.email);
        password = (AppCompatEditText) findViewById(R.id.password);
        login = (MaterialButton) findViewById(R.id.login);
        loading = (RelativeLayout) findViewById(R.id.loading);
        sessionManager = new SessionManager(Login.this);

        if (!sessionManager.getUsername().equals("kosong")){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                AndroidNetworking.post(sessionManager.getServer() + "api/login")
                        .addBodyParameter("email", Objects.requireNonNull(email.getText()).toString())
                        .addBodyParameter("password", Objects.requireNonNull(password.getText()).toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("RESPONSE", response.toString());
                                try {
                                    if (response.get("kode").equals("200")) {
                                        loading.setVisibility(View.GONE);
                                        sessionManager.setUsername(response.getJSONObject("data").getJSONObject("user").get("email").toString());
                                        sessionManager.setNama(response.getJSONObject("data").getJSONObject("user").get("nama").toString());
                                        sessionManager.setToken(response.getJSONObject("data").get("token").toString());
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (response.get("kode").equals("411")) {
                                        String[] rule = {"email", "password"};
                                        String pesanvalidasi = new PesanValidasi(rule, response).getpesan();
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(Login.this)
                                                .setMessage(pesanvalidasi)
                                                .show();
                                    } else if (response.get("kode").equals("406")){
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(Login.this)
                                                .setMessage(response.get("pesan").toString())
                                                .show();

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
        periksa_keaslian_dokumen = (AppCompatTextView) findViewById(R.id.periksa_keaslian_dokumen);
        periksa_keaslian_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, PeriksaKeaslianDokumen.class);
                startActivity(intent);
            }
        });
        register = (AppCompatTextView) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });

        teks_kebijakan = (AppCompatTextView) findViewById(R.id.teks_kebijakan);
        teks_kebijakan.setMovementMethod(LinkMovementMethod.getInstance());
    }
}