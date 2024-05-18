package com.kickymaulana.com.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.kickymaulana.com.tandatangandigital.penolong.HashFileHelper;

import android.net.Uri;
import android.widget.RelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;


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
                    if (i < hasil_hash.length() - 1){
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
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("application/pdf");
                startActivityForResult(intent, PICK_PDF_FILE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                s_link_pdf = uri.getPath();
                nama_file.setText(uri.getPath());
            }
        }
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemid = item.getItemId();
        if (itemid == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}