package com.kickymaulana.com.tandatangandigital.adapter;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.kickymaulana.com.tandatangandigital.BilanganPrima;
import com.kickymaulana.com.tandatangandigital.BilanganRelatifPrima;
import com.kickymaulana.com.tandatangandigital.R;
import com.kickymaulana.com.tandatangandigital.model.BilanganPrimaModel;
import com.kickymaulana.com.tandatangandigital.model.BilanganRelatifPrimaModel;

import java.util.List;


public class BilanganRelatifPrimaAdapter extends RecyclerView.Adapter<BilanganRelatifPrimaAdapter.BilanganRelatifPrimaHolder>{
    Context context;
    List<BilanganRelatifPrimaModel> bilanganRelatifPrimaModelList;

    public BilanganRelatifPrimaAdapter(Context context, List<BilanganRelatifPrimaModel> bilanganRelatifPrimaModelList) {
        this.context = context;
        this.bilanganRelatifPrimaModelList = bilanganRelatifPrimaModelList;
    }

    @NonNull
    @Override
    public BilanganRelatifPrimaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bilangan_relatif_prima, parent, false);
        return new BilanganRelatifPrimaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BilanganRelatifPrimaHolder holder, int position) {

        holder.bilangan_relatif_prima.setText(bilanganRelatifPrimaModelList.get(position).getBilangan_relatif_prima());
        holder.card_bilangan_relatif_prima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("bilangan_relatif_prima", bilanganRelatifPrimaModelList.get(position).getBilangan_relatif_prima());
                ((BilanganRelatifPrima) context).setResult(RESULT_OK, intent);
                ((BilanganRelatifPrima) context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return bilanganRelatifPrimaModelList.size();
    }

    class BilanganRelatifPrimaHolder extends RecyclerView.ViewHolder{

        CardView card_bilangan_relatif_prima;
        AppCompatTextView bilangan_relatif_prima;

        public BilanganRelatifPrimaHolder(@NonNull View itemView) {
            super(itemView);

            card_bilangan_relatif_prima = (CardView) itemView.findViewById(R.id.card_bilangan_relatif_prima);
            bilangan_relatif_prima = (AppCompatTextView) itemView.findViewById(R.id.bilangan_relatif_prima);
        }
    }
}
