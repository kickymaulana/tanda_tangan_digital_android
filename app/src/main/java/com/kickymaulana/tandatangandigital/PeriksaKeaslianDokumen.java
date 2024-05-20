package com.kickymaulana.tandatangandigital;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
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

    String CHIPERTEXT;
    String PLAINTEXT;
    String SHA256 = "kosong";

    MaterialButton periksa_keaslian_dokumen;

    BigInteger e_big;
    BigInteger n_big;

    AppCompatEditText bilangan_e;
    AppCompatEditText bilangan_n;
    private ActivityResultLauncher<Intent> daftar_kunci_publik_launcher;
    MaterialButton daftar_kunci_publik;

    String nama_penanda_tangan;
    private static final int REQUEST_PERMISSIONS_CODE = 1;

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

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        requestPermissions();

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
                        try {
                            String hash = getSHA256FromUri(uri);
                            Log.d("MainActivity", "Selected PDF URI: " + uri);
                            Log.d("MainActivity", "SHA-256 Hash: " + hash);
                            SHA256 = hash;
                        } catch (IOException | NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
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
                            Log.d("HASIL_JSON", jsonObject.getJSONObject("signature").get("data").toString());
                            CHIPERTEXT = jsonObject.getJSONObject("signature").get("data").toString();
                            nama_penanda_tangan = jsonObject.getJSONObject("signature").get("nama").toString();

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

        bilangan_e = (AppCompatEditText) findViewById(R.id.bilangan_e);
        bilangan_n = (AppCompatEditText) findViewById(R.id.bilangan_n);

        periksa_keaslian_dokumen = (MaterialButton) findViewById(R.id.periksa_keaslian_dokumen);
        periksa_keaslian_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] dataArray = CHIPERTEXT.split("\\.");

                String hash_hasil_deskripsi = "";
                for (String value : dataArray) {
                    BigInteger c_big = new BigInteger(value);
                    e_big = new BigInteger(bilangan_e.getText().toString());
                    n_big = new BigInteger(bilangan_n.getText().toString());
                    BigInteger m_big = c_big.modPow(e_big, n_big);
                    System.out.println(value + " = " + m_big + " ascci : " + (char) m_big.intValue());
                    hash_hasil_deskripsi = hash_hasil_deskripsi + (char) m_big.intValue();
                }
                PLAINTEXT = hash_hasil_deskripsi;
                Log.d("PLAINTEXT", PLAINTEXT);
                Log.d("PLAINTEXTSHA256", SHA256);

                if (PLAINTEXT.equals(SHA256)){
                    new AlertDialog.Builder(PeriksaKeaslianDokumen.this)
                            .setMessage("File asli dan sudah ditandatangani oleh " + nama_penanda_tangan)
                            .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    new AlertDialog.Builder(PeriksaKeaslianDokumen.this)
                            .setMessage("File tidak asli")
                            .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }

            }
        });

        daftar_kunci_publik_launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        bilangan_e.setText(data.getStringExtra("bilangan_e"));
                        bilangan_n.setText(data.getStringExtra("bilangan_n"));
                    }
                }

            }
        });

        daftar_kunci_publik = (MaterialButton) findViewById(R.id.daftar_kunci_publik);
        daftar_kunci_publik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PeriksaKeaslianDokumen.this, DaftarKunciPublik.class);
                daftar_kunci_publik_launcher.launch(intent);

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

    //kode yang baru di tambahkan sampai kebawah
    private String getSHA256FromUri(Uri uri) throws IOException, NoSuchAlgorithmException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("Unable to open input stream from URI");
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        inputStream.close();

        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 dan yang lebih baru
            String[] permissions = {
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    android.Manifest.permission.READ_MEDIA_AUDIO
            };

            requestPermissionsIfNeeded(permissions);
        } else {
            // Android 10 dan yang lebih lama
            String[] permissions = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };

            requestPermissionsIfNeeded(permissions);
        }
    }

    private void requestPermissionsIfNeeded(String[] permissions) {
        boolean permissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsGranted = false;
                break;
            }
        }

        if (!permissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    // Jika ada izin yang tidak diberikan
                    // Tangani kasus ini sesuai kebutuhan aplikasi Anda
                    return;
                }
            }
            // Semua izin diberikan
        }
    }
}