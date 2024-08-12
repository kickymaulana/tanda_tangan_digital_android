package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

public class DokumenShow extends AppCompatActivity {

    MaterialButton download_dokumen;
    MaterialButton download_signature;
    AppCompatTextView nama;
    AppCompatTextView dokumen;
    AppCompatTextView signature;
    AppCompatTextView ditandatangani;

    SessionManager sessionManager;
    RelativeLayout loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dokumen_show);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Detail Dokumen");

        Intent intent = getIntent();

        download_dokumen = (MaterialButton) findViewById(R.id.download_dokumen);
        download_signature = (MaterialButton) findViewById(R.id.download_signature);
        nama = (AppCompatTextView) findViewById(R.id.nama);
        dokumen = (AppCompatTextView) findViewById(R.id.dokumen);
        signature = (AppCompatTextView) findViewById(R.id.signature);
        ditandatangani = (AppCompatTextView) findViewById(R.id.ditandatangani);
        sessionManager = new SessionManager(DokumenShow.this);
        loading = (RelativeLayout) findViewById(R.id.loading);

        nama.setText(intent.getStringExtra("nama"));
        dokumen.setText(intent.getStringExtra("dokumen"));
        signature.setText(intent.getStringExtra("signature"));
        ditandatangani.setText(intent.getStringExtra("ditandatangani"));
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}