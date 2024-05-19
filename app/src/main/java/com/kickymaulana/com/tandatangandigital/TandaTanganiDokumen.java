package com.kickymaulana.com.tandatangandigital;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ChangedPackages;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.atwa.filepicker.core.FilePicker;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.karan.churi.PermissionManager.PermissionManager;
import com.kickymaulana.com.tandatangandigital.penolong.HashFileHelper;
import com.kickymaulana.com.tandatangandigital.penolong.ImageUriToFile;

import android.net.Uri;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Objects;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import me.rosuh.filepicker.config.FilePickerManager;


public class TandaTanganiDokumen extends AppCompatActivity {

    MaterialButton tandatangani_dokumen;
    MaterialButton pilih_dokumen;
    AppCompatTextView nama_file;
    String s_link_pdf;
    BigInteger d_big;
    BigInteger n_big;
    AppCompatEditText bilangan_d;
    AppCompatEditText bilangan_n;

    String SHA256 = "kosong";
    String CHIPERTEXT;
    Uri uri_hasil;
    private String stringToSave;

    private static final String PERMISSON_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final int PERMISSON_REQ_CODE = 100;



    private ActivityResultLauncher<Intent> openFileLauncher;
    private ActivityResultLauncher<Intent> createFileLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tanda_tangani_dokumen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
                    }
                }

            }
        });

        createFileLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri uri = data.getData();
                        try {
                            saveStringToFile(uri, stringToSave);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        // Initialize result launcher

        requestRuntimePermission();


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tandatangani Dokumen");

        bilangan_d = (AppCompatEditText) findViewById(R.id.bilangan_d);
        bilangan_n = (AppCompatEditText) findViewById(R.id.bilangan_n);

        pilih_dokumen = (MaterialButton) findViewById(R.id.pilih_dokumen);
        pilih_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                openFileLauncher.launch(intent);

            }
        });


        tandatangani_dokumen = (MaterialButton) findViewById(R.id.tandatangani_dokumen);
        tandatangani_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                d_big = new BigInteger(bilangan_d.getText().toString());
                n_big = new BigInteger(bilangan_n.getText().toString());
                String chipertext = "";
                for (int i = 0; i < SHA256.length(); i++) {
                    char character = SHA256.charAt(i);
                    int kode_ascii = (int) character;
                    String kode_ascii3digit = String.format("%03d", kode_ascii);
                    BigInteger m = new BigInteger(kode_ascii3digit);
                    BigInteger c = m.modPow(d_big, n_big);
                    if (i < SHA256.length() - 1) {
                        chipertext = chipertext + c.intValue() + ".";
                    } else {
                        chipertext = chipertext + c.intValue();
                    }
                }
                System.out.println("chipertext sekarang : " + chipertext);
                CHIPERTEXT = chipertext;
                JSONObject signatureObject = new JSONObject();
                try {
                    signatureObject.put("nama", "Kicky Maulana");
                    signatureObject.put("data", CHIPERTEXT);
                    JSONObject finalObject = new JSONObject();
                    finalObject.put("signature", signatureObject);
                    Log.d("FINALOBJECT", finalObject.toString());
                    stringToSave = finalObject.toString();
                    new androidx.appcompat.app.AlertDialog.Builder(TandaTanganiDokumen.this)
                            .setMessage("File berhasil ditandatangani, silahkan simpan file tandatangan anda")
                            .setCancelable(false)
                            .setPositiveButton("Simpan file tandatangan", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    createFile(getFileNameFromUri(uri_hasil) + ".txt");

                                }
                            })
                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        nama_file = (AppCompatTextView) findViewById(R.id.nama_file);
        nama_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TandaTanganiDokumen.this, TampilPdf.class);
                intent.putExtra("s_link_pdf", s_link_pdf);
                startActivity(intent);
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

    private void requestRuntimePermission() {
        if (ActivityCompat.checkSelfPermission(this, PERMISSON_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted kamu di izin kan menggunakan fitur ini", Toast.LENGTH_SHORT).show();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSON_READ_EXTERNAL_STORAGE)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("fitur ini membutuhkan izin read external storage")
                    .setTitle("permission dibutuhkan")
                    .setCancelable(false)
                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(TandaTanganiDokumen.this, new String[]{PERMISSON_READ_EXTERNAL_STORAGE}, PERMISSON_REQ_CODE);
                            dialog.dismiss();

                        }
                    })
                    .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{PERMISSON_READ_EXTERNAL_STORAGE}, PERMISSON_REQ_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSON_REQ_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted kamu di izin kan menggunakan fitur ini", Toast.LENGTH_SHORT).show();

            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSON_READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("fiture ini tidak tersedia")
                        .setTitle("permisi dibutuhkan")
                        .setCancelable(false)
                        .setNegativeButton("batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        })
                        .show();

            } else {
                requestRuntimePermission();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FilePickerManager.REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                s_link_pdf = FilePickerManager.obtainData().get(0).toString();
                nama_file.setText(s_link_pdf);

            } else {
                Toast.makeText(this, "You didn't choose anything~", Toast.LENGTH_SHORT).show();
            }

        }
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

    private void createFile(String nama_file_tandatangan) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, nama_file_tandatangan);
        createFileLauncher.launch(intent);
    }

    private void saveStringToFile(Uri uri, String content) throws IOException {
        try (OutputStream outputStream = getContentResolver().openOutputStream(uri)) {
            if (outputStream != null) {
                outputStream.write(content.getBytes());
                Log.d("MainActivity", "File saved successfully");
            } else {
                throw new IOException("Unable to open output stream for URI");
            }
        }
    }


}