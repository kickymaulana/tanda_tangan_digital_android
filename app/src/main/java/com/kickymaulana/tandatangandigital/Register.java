package com.kickymaulana.tandatangandigital;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
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
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Register extends AppCompatActivity {

    AppCompatEditText email;
    AppCompatEditText nama;
    AppCompatEditText password;
    AppCompatEditText password_confirm;
    MaterialButton register;
    RelativeLayout loading;
    SessionManager sessionManager;
    AppCompatTextView teks_kebijakan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Register");

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        email = (AppCompatEditText) findViewById(R.id.email);
        nama = (AppCompatEditText) findViewById(R.id.nama);
        password = (AppCompatEditText) findViewById(R.id.password);
        password_confirm = (AppCompatEditText) findViewById(R.id.password_confirm);
        register = (MaterialButton) findViewById(R.id.register);
        loading = (RelativeLayout) findViewById(R.id.loading);
        sessionManager = new SessionManager(Register.this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.setVisibility(View.VISIBLE);
                AndroidNetworking.post(sessionManager.getServer() + "api/register")
                        .addBodyParameter("email", Objects.requireNonNull(email.getText()).toString())
                        .addBodyParameter("nama", Objects.requireNonNull(nama.getText()).toString())
                        .addBodyParameter("password", Objects.requireNonNull(password.getText()).toString())
                        .addBodyParameter("password_confirmation", Objects.requireNonNull(password_confirm.getText()).toString())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("RESPONSE", response.toString());
                                try {
                                    if (response.get("kode").equals("200")) {
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(Register.this)
                                                .setMessage(response.get("pesan").toString())
                                                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                })
                                                .show();
                                    } else if (response.get("kode").equals("411")) {
                                        String[] rule = {"email", "nama", "password"};
                                        String pesanvalidasi = new PesanValidasi(rule, response).getpesan();
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(Register.this)
                                                .setMessage(pesanvalidasi)
                                                .show();
                                    } else if (response.get("kode").equals("406")){
                                        loading.setVisibility(View.GONE);
                                        new AlertDialog.Builder(Register.this)
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
        teks_kebijakan = (AppCompatTextView) findViewById(R.id.teks_kebijakan);
        teks_kebijakan.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}