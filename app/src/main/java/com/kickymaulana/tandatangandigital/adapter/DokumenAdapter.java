package com.kickymaulana.tandatangandigital.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kickymaulana.tandatangandigital.R;
import com.kickymaulana.tandatangandigital.model.DokumenModel;

import java.util.List;


public class DokumenAdapter extends RecyclerView.Adapter<DokumenAdapter.DokumenHolder>{
    Context context;
    List<DokumenModel> dokumenModelList;

    public DokumenAdapter(Context context, List<DokumenModel> dokumenModelList) {
        this.context = context;
        this.dokumenModelList = dokumenModelList;
    }

    @NonNull
    @Override
    public DokumenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_dokumen, parent, false);
        return new DokumenHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DokumenHolder holder, int position) {

        holder.nama.setText(dokumenModelList.get(position).getNama());
        holder.dokumen.setText(dokumenModelList.get(position).getDokumen());
        holder.signature.setText(dokumenModelList.get(position).getSignature());
        holder.ditandatangani.setText(dokumenModelList.get(position).getDitandatangani());
        holder.card_dokumen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.putExtra("bilangan_e", kunciPublikModelList.get(position).getBilangan_e());
                intent.putExtra("bilangan_n", kunciPublikModelList.get(position).getBilangan_n());
                ((DaftarKunciPublik) context).setResult(RESULT_OK, intent);
                ((DaftarKunciPublik) context).finish();*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return dokumenModelList.size();
    }

    class DokumenHolder extends RecyclerView.ViewHolder{

        CardView card_dokumen;
        AppCompatTextView dokumen, signature, nama, ditandatangani;

        public DokumenHolder(@NonNull View itemView) {
            super(itemView);

            card_dokumen = (CardView) itemView.findViewById(R.id.card_dokumen);
            dokumen = (AppCompatTextView) itemView.findViewById(R.id.dokumen);
            signature = (AppCompatTextView) itemView.findViewById(R.id.signature);
            nama = (AppCompatTextView) itemView.findViewById(R.id.nama);
            ditandatangani = (AppCompatTextView) itemView.findViewById(R.id.ditandatangani);
        }
    }
}
