package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;

public class TampilPdf extends AppCompatActivity{

    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
    String url = "kosong";
    RelativeLayout loading;

    PDFViewPager pdfViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tampil_pdf);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        Intent intent = getIntent();
        url = intent.getStringExtra("s_link_pdf");
        Log.d("tewting", url);
        File file = new File(url);
        pdfViewPager = (PDFViewPager) findViewById(R.id.pdfViewPager);
        pdfViewPager = new PDFViewPager(this, url);
    }

}