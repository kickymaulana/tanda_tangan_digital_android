package com.kickymaulana.tandatangandigital;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.kickymaulana.tandatangandigital.adapter.DokumenAdapter;
import com.kickymaulana.tandatangandigital.adapter.KunciPublikAdapter;
import com.kickymaulana.tandatangandigital.model.DokumenModel;
import com.kickymaulana.tandatangandigital.model.KunciPublikModel;
import com.kickymaulana.tandatangandigital.sessionmanager.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dokumen extends AppCompatActivity {

    List<DokumenModel> dokumenModelList;
    DokumenAdapter dokumenAdapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    RelativeLayout loading;
    com.cjj.MaterialRefreshLayout material_refresh_layout;
    Integer offset = 0, limit = 20;
    SessionManager sessionManager;
    AppCompatTextView query_cari;
    CardView card_status_cari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dokumen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("List Dokumen");

        dokumenModelList = new ArrayList<>();
        dokumenAdapter = new DokumenAdapter(Dokumen.this, dokumenModelList);
        linearLayoutManager = new LinearLayoutManager(Dokumen.this);
        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(dokumenAdapter);
        loading = (RelativeLayout) findViewById(R.id.loading);
        material_refresh_layout = (MaterialRefreshLayout) findViewById(R.id.material_refresh_layout);
        material_refresh_layout.setLoadMore(true);
        sessionManager = new SessionManager(Dokumen.this);
        query_cari = (AppCompatTextView) findViewById(R.id.query_cari);
        card_status_cari = (CardView) findViewById(R.id.card_status_cari);
        card_status_cari.setVisibility(View.GONE);

        loading.setVisibility(View.VISIBLE);
        AndroidNetworking.post(sessionManager.getServer() + "api/dokumen/index")
                .addBodyParameter("offset", String.valueOf(offset))
                .addBodyParameter("limit", String.valueOf(limit))
                .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("RESPONSEBARU", response.toString());
                            if (response.get("kode").equals("200")) {
                                dokumenModelList.clear();
                                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                    String id = response.getJSONArray("data").getJSONObject(i).get("id").toString();
                                    String nama = response.getJSONArray("data").getJSONObject(i).get("nama").toString();
                                    String dokumen = response.getJSONArray("data").getJSONObject(i).get("dokumen").toString();
                                    String signature = response.getJSONArray("data").getJSONObject(i).get("signature").toString();
                                    String ditandatangani = response.getJSONArray("data").getJSONObject(i).get("ditandatangani").toString();
                                    dokumenModelList.add(new DokumenModel(id, nama, dokumen, signature, ditandatangani));
                                }
                                dokumenAdapter.notifyDataSetChanged();
                                offset = offset + limit;
                                loading.setVisibility(View.GONE);
                            } else if (response.get("kode").equals("401")) {
                                loading.setVisibility(View.GONE);
                                sessionManager.logout();
                                Intent intent = new Intent(Dokumen.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();

                    }
                });


        material_refresh_layout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                dokumenModelList.clear();
                offset = 0;
                AndroidNetworking.post(sessionManager.getServer() + "api/dokumen/index")
                        .addBodyParameter("offset", String.valueOf(offset))
                        .addBodyParameter("limit", String.valueOf(limit))
                        .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("kode").equals("200")) {
                                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                            String id = response.getJSONArray("data").getJSONObject(i).get("id").toString();
                                            String nama = response.getJSONArray("data").getJSONObject(i).get("nama").toString();
                                            String dokumen = response.getJSONArray("data").getJSONObject(i).get("dokumen").toString();
                                            String signature = response.getJSONArray("data").getJSONObject(i).get("signature").toString();
                                            String ditandatangani = response.getJSONArray("data").getJSONObject(i).get("ditandatangani").toString();
                                            dokumenModelList.add(new DokumenModel(id, nama, dokumen, signature, ditandatangani));
                                        }
                                        dokumenAdapter.notifyDataSetChanged();
                                        offset = offset + limit;
                                        materialRefreshLayout.finishRefresh();
                                    } else if (response.get("kode").equals("401")) {
                                        loading.setVisibility(View.GONE);
                                        sessionManager.logout();
                                        Intent intent = new Intent(Dokumen.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Log.d("RESEPONSEANEH", response.toString());
                                        Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                materialRefreshLayout.finishRefresh();
                                Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                            }
                        });

            }


            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                AndroidNetworking.post(sessionManager.getServer() + "api/dokumen/index")
                        .addBodyParameter("offset", String.valueOf(offset))
                        .addBodyParameter("limit", String.valueOf(limit))
                        .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("kode").equals("200")) {

                                        for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                            String id = response.getJSONArray("data").getJSONObject(i).get("id").toString();
                                            String nama = response.getJSONArray("data").getJSONObject(i).get("nama").toString();
                                            String dokumen = response.getJSONArray("data").getJSONObject(i).get("dokumen").toString();
                                            String signature = response.getJSONArray("data").getJSONObject(i).get("signature").toString();
                                            String ditandatangani = response.getJSONArray("data").getJSONObject(i).get("ditandatangani").toString();
                                            dokumenModelList.add(new DokumenModel(id, nama, dokumen, signature, ditandatangani));
                                        }
                                        dokumenAdapter.notifyDataSetChanged();
                                        offset = offset + limit;
                                        materialRefreshLayout.finishRefreshLoadMore();
                                    } else if (response.get("kode").equals("401")) {
                                        loading.setVisibility(View.GONE);
                                        sessionManager.logout();
                                        Intent intent = new Intent(Dokumen.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                                        loading.setVisibility(View.GONE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                materialRefreshLayout.finishRefreshLoadMore();
                                Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                            }
                        });
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.daftar_kunci_publik, menu);

        MenuItem cari = menu.findItem(R.id.cari);
        SearchView searchView = (SearchView) cari.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cari(query);
                query_cari.setText("query pencarian '" + query + "'");
                card_status_cari.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void cari(String cari) {
        sessionManager.setCari(cari);
        loading.setVisibility(View.VISIBLE);
        offset = 0;
        AndroidNetworking.post(sessionManager.getServer() + "api/dokumen/index")
                .addBodyParameter("offset", String.valueOf(offset))
                .addBodyParameter("limit", String.valueOf(limit))
                .addBodyParameter("cari", cari)
                .addHeaders("Authorization", "Bearer " + sessionManager.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("kode").equals("200")) {
                                dokumenModelList.clear();
                                for (int i = 0; i < response.getJSONArray("data").length(); i++) {
                                    String id = response.getJSONArray("data").getJSONObject(i).get("id").toString();
                                    String nama = response.getJSONArray("data").getJSONObject(i).get("nama").toString();
                                    String dokumen = response.getJSONArray("data").getJSONObject(i).get("dokumen").toString();
                                    String signature = response.getJSONArray("data").getJSONObject(i).get("signature").toString();
                                    String ditandatangani = response.getJSONArray("data").getJSONObject(i).get("ditandatangani").toString();
                                    dokumenModelList.add(new DokumenModel(id, nama, dokumen, signature, ditandatangani));
                                }
                                dokumenAdapter.notifyDataSetChanged();
                                offset = offset + limit;
                                loading.setVisibility(View.GONE);
                            } else if (response.get("kode").equals("401")) {
                                loading.setVisibility(View.GONE);
                                sessionManager.logout();
                                Intent intent = new Intent(Dokumen.this, Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                                loading.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(Dokumen.this, "ada kesalahan lain", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}