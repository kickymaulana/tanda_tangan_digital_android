package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.androidnetworking.AndroidNetworking;
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.com.tandatangandigital.sessionmanager.SessionManager;

public class MainActivity extends AppCompatActivity {

    MaterialButton logout;
    MaterialButton bangkitkan_kunci;
    SessionManager sessionManager;

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

        bangkitkan_kunci = (MaterialButton) findViewById(R.id.bangkitkan_kunci);
        bangkitkan_kunci.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BangkitkanKunci.class);
                startActivity(intent);
            }
        });

        logout = (MaterialButton) findViewById(R.id.logout);
        sessionManager = new SessionManager(MainActivity.this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });


    }
}