package com.kickymaulana.com.tandatangandigital.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kickymaulana.com.tandatangandigital.DaftarKunciPublik;
import com.kickymaulana.com.tandatangandigital.R;
import com.kickymaulana.com.tandatangandigital.model.KunciPublikModel;

import java.util.List;


public class KunciPublikAdapter extends RecyclerView.Adapter<KunciPublikAdapter.KunciPublikHolder>{
    Context context;
    List<KunciPublikModel> kunciPublikModelList;

    public KunciPublikAdapter(Context context, List<KunciPublikModel> kunciPublikModelList) {
        this.context = context;
        this.kunciPublikModelList = kunciPublikModelList;
    }

    @NonNull
    @Override
    public KunciPublikHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kunci_publik, parent, false);
        return new KunciPublikHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KunciPublikHolder holder, int position) {

        holder.nama.setText(kunciPublikModelList.get(position).getNama());
        holder.kunci_publik.setText("Kunci publik (e = " + kunciPublikModelList.get(position).getBilangan_e() + ", n = " + kunciPublikModelList.get(position).getBilangan_n() + ")");
        /*holder.card_jadwal_hari_kerja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JadwalHariKerjaShow.class);
                intent.putExtra("id", jadwalHariKerjaModelList.get(position).getId());
                //((DaftarKunciPublik) context).tambah_launcher.launch(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return kunciPublikModelList.size();
    }

    class KunciPublikHolder extends RecyclerView.ViewHolder{

        CardView card_kunci_publik;
        AppCompatTextView nama, kunci_publik;

        public KunciPublikHolder(@NonNull View itemView) {
            super(itemView);

            card_kunci_publik = (CardView) itemView.findViewById(R.id.card_kunci_publik);
            nama = (AppCompatTextView) itemView.findViewById(R.id.nama);
            kunci_publik = (AppCompatTextView) itemView.findViewById(R.id.kunci_publik);
        }
    }
}
