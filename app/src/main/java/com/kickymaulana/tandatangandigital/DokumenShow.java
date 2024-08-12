package com.kickymaulana.tandatangandigital;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

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
    MaterialButton bagikan;
    MaterialButton salin_link;
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
        bagikan = (MaterialButton) findViewById(R.id.bagikan);
        salin_link = (MaterialButton) findViewById(R.id.salin_link);
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

        download_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URL yang ingin dibuka
                String url = sessionManager.getServer() + "storage/dokumen/" + dokumen.getText().toString();

                // Intent untuk membuka link
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Mulai aktivitas
                startActivity(intent);
            }
        });

        download_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // URL yang ingin dibuka
                String url = sessionManager.getServer() + "signature/" + signature.getText().toString();

                // Intent untuk membuka link
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));

                // Mulai aktivitas
                startActivity(intent);
            }
        });

        bagikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wa = new Intent(Intent.ACTION_SEND);
                wa.setType("text/plain");
                wa.putExtra(Intent.EXTRA_TEXT, "Cek Tandatangan Ku Disini " + sessionManager.getServer() + "dokumen-dan-signature/" + intent.getStringExtra("id"));
                startActivity(wa);
            }
        });

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        salin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creates a new text clip to put on the clipboard.
                ClipData clip = ClipData.newPlainText("Link Dokumen dan Signature",  sessionManager.getServer() + "dokumen-dan-signature/" + intent.getStringExtra("id"));
                // Set the clipboard's primary clip.
                clipboard.setPrimaryClip(clip);
                Toast.makeText(DokumenShow.this,  clip.getItemAt(0).getText(), Toast.LENGTH_SHORT).show();
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