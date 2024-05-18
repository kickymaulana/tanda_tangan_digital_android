package com.kickymaulana.com.tandatangandigital;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.DocumentsContract;
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
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Objects;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import me.rosuh.filepicker.config.FilePickerManager;


public class TandaTanganiDokumen extends AppCompatActivity {

    MaterialButton tandatangani_dokumen;
    MaterialButton pilih_dokumen;
    private static final int PICK_PDF_FILE = 2;
    AppCompatTextView nama_file;
    String s_link_pdf;
    String hasil_hash;
    BigInteger d_big;
    BigInteger n_big;
    AppCompatEditText bilangan_d;
    AppCompatEditText bilangan_n;

    private static final String PERMISSON_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final int PERMISSON_REQ_CODE = 100;

    private ActivityResultLauncher<Intent> launcher; // Initialise this object in Activity.onCreate()
    private Uri baseDocumentTreeUri;

    public void launchBaseDirectoryPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        launcher.launch(intent);
    }


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

        requestRuntimePermission();


        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Tandatangani Dokumen");

        bilangan_d = (AppCompatEditText) findViewById(R.id.bilangan_d);
        bilangan_n = (AppCompatEditText) findViewById(R.id.bilangan_n);


        tandatangani_dokumen = (MaterialButton) findViewById(R.id.tandatangani_dokumen);
        tandatangani_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageManager storageManager = (StorageManager) getSystemService(STORAGE_SERVICE);
                StorageVolume storageVolume = storageManager.getStorageVolumes().get(0);
                /*File fileInputImage = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    fileInputImage = new File(storageVolume.getDirectory().getPath() + "/Download/images.png");
                }
                Bitmap bitmapInputImage = BitmapFactory.decodeFile(fileInputImage.getPath());

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapInputImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bytesArray = byteArrayOutputStream.toByteArray();*/

                /*File fileOutput = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                    fileOutput = new File(storageVolume.getDirectory().getPath() + "/Download/image1.png");
                }
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(fileOutput);
                    try {
                        fileOutputStream.write(bytesArray);
                        fileOutputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }*/


                HashFileHelper hashFile = new HashFileHelper();
                hasil_hash = hashFile.hash_file(s_link_pdf);
                Log.d("HASIL_HASH", hasil_hash);
                d_big = new BigInteger(bilangan_d.getText().toString());
                n_big = new BigInteger(bilangan_n.getText().toString());
                String chipertext = "";
                for (int i = 0; i < hasil_hash.length(); i++) {
                    char character = hasil_hash.charAt(i);
                    int kode_ascii = (int) character;
                    String kode_ascii3digit = String.format("%03d", kode_ascii);
                    BigInteger m = new BigInteger(kode_ascii3digit);
                    BigInteger c = m.modPow(d_big, n_big);
                    if (i < hasil_hash.length() - 1) {
                        chipertext = chipertext + c.intValue() + ".";
                    } else {
                        chipertext = chipertext + c.intValue();
                    }
                }
                System.out.println("chipertext sekarang : " + chipertext);
                JSONObject signatureObject = new JSONObject();
                try {
                    signatureObject.put("nama", "Kicky Maulana");
                    signatureObject.put("data", chipertext);
                    JSONObject finalObject = new JSONObject();
                    finalObject.put("signature", signatureObject);
                    Log.d("FINALOBJECT", finalObject.toString());
                    //menyimpan file di folder download
                    File gpxfile = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                        File namafile = new File(s_link_pdf);
                        Log.d("NAMAFILE", namafile.getName());
                        gpxfile = new File(storageVolume.getDirectory().getPath() + "/Download/", namafile.getName() + ".txt");
                    }
                    FileWriter writer = null;
                    try {
                        writer = new FileWriter(gpxfile);
                        writer.append(finalObject.toString());
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }




                // Menulis teks ke dalam file
                /*File namaFile = new File(file.getParent() + "\\" + getFileNameWithoutExtension(file) + ".sig");
                System.out.println(file.getParent() + "\\" + getFileNameWithoutExtension(file) + ".sig");
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(namaFile))) {
                    writer.write(finalObject.toString());
                    System.out.println("Teks berhasil ditulis ke dalam file " + namaFile);
                } catch (IOException e) {
                    System.err.println("Gagal menulis ke file: " + e.getMessage());
                }*/

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

        pilih_dokumen = (MaterialButton) findViewById(R.id.pilih_dokumen);
        pilih_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilePickerManager
                        .from(TandaTanganiDokumen.this)
                        .forResult(FilePickerManager.REQUEST_CODE);

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

    private void requestRuntimePermission(){
        if (ActivityCompat.checkSelfPermission(this, PERMISSON_READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permission Granted kamu di izin kan menggunakan fitur ini", Toast.LENGTH_SHORT).show();
        } else if(ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSON_READ_EXTERNAL_STORAGE)){
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
        if (requestCode == PERMISSON_REQ_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted kamu di izin kan menggunakan fitur ini", Toast.LENGTH_SHORT).show();

            } else if(!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSON_READ_EXTERNAL_STORAGE)){
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
        if (requestCode == FilePickerManager.REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                s_link_pdf = FilePickerManager.obtainData().get(0).toString();
                nama_file.setText(s_link_pdf);

            } else {
                Toast.makeText(this, "You didn't choose anything~", Toast.LENGTH_SHORT).show();
            }

        }
    }

}