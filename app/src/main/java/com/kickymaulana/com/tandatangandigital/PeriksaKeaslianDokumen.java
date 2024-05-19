package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.MenuItem;
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

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

public class PeriksaKeaslianDokumen extends AppCompatActivity {

    MaterialButton pilih_dokumen;
    MaterialButton pilih_signature;

    private ActivityResultLauncher<Intent> openFileLauncher;
    private ActivityResultLauncher<Intent> openFileLauncher2;
    Uri uri_hasil;
    Uri uri_hasil2;
    AppCompatTextView nama_dokumen;
    AppCompatTextView nama_signature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_periksa_keaslian_dokumen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Periksa Keaslian Dokumen");

        pilih_dokumen = (MaterialButton) findViewById(R.id.pilih_dokumen);
        pilih_signature = (MaterialButton) findViewById(R.id.pilih_signature);
        nama_dokumen = (AppCompatTextView) findViewById(R.id.nama_dokumen);
        nama_signature = (AppCompatTextView) findViewById(R.id.nama_signature);


        openFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        uri_hasil = uri;
                        nama_dokumen.setText(getFileNameFromUri(uri_hasil));
                    }
                }

            }
        });

        openFileLauncher2 = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        uri_hasil2 = uri;
                        nama_signature.setText(getFileNameFromUri(uri_hasil2));
                        String content = null;
                        try {
                            content = readTextFromUri(uri_hasil2);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Log.d("MainActivity", "Selected TXT URI: " + uri);
                        Log.d("MainActivity", "File Content: " + content);

                        try {
                            JSONObject jsonObject = convertToJSON(content);
                            Log.d("HASIL_JSON", jsonObject.toString());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

            }
        });
        pilih_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                openFileLauncher.launch(intent);

            }
        });

        pilih_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/plain");
                openFileLauncher2.launch(intent);

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

    private String getFileNameFromUri(Uri uri) {
        String fileName = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        fileName = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.getPath();
            int cut = fileName.lastIndexOf('/');
            if (cut != -1) {
                fileName = fileName.substring(cut + 1);
            }
        }
        return fileName;
    }
    private String readTextFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Unable to open input stream from URI");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }

        inputStream.close();
        return stringBuilder.toString();
    }

    private JSONObject convertToJSON(String content) throws JSONException {
        return new JSONObject(content);
    }
}