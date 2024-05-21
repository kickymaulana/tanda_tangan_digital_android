package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.kickymaulana.tandatangandigital.model.KunciPublikModel;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class AmbilKunciPrivat extends AppCompatActivity {

    SessionManager sessionManager;
    RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ambil_kunci_privat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sessionManager = new SessionManager(AmbilKunciPrivat.this);
        loading = (RelativeLayout) findViewById(R.id.loading);

        loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(sessionManager.getServer() + "api/ambil-kunci-privat")
                .addBodyParameter("email", String.valueOf(sessionManager.getUsername()))
                .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSEBARU", response.toString());
                            if (response.get("kode").equals("200")) {
                                loading.setVisibility(View.GONE);
                                Intent intent = new Intent();
                                intent.putExtra("bilangan_d", response.getJSONObject("data").get("bilangan_d").toString());
                                intent.putExtra("bilangan_n", response.getJSONObject("data").get("bilangan_n").toString());
                                setResult(RESULT_OK, intent);
                                finish();
                            } else if (response.get("kode").equals("401")) {
                                loading.setVisibility(View.GONE);
                                sessionManager.logout();
                                Intent intent = new Intent(AmbilKunciPrivat.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(AmbilKunciPrivat.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(AmbilKunciPrivat.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();

                    }
                });
    }
}