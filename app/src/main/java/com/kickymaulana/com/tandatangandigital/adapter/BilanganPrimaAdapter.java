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
import com.kickymaulana.com.tandatangandigital.R;
import com.kickymaulana.com.tandatangandigital.model.BilanganPrimaModel;

import java.util.List;


public class BilanganPrimaAdapter extends RecyclerView.Adapter<BilanganPrimaAdapter.BilanganPrimaHolder>{
    Context context;
    List<BilanganPrimaModel> bilanganPrimaModelList;

    public BilanganPrimaAdapter(Context context, List<BilanganPrimaModel> bilanganPrimaModelList) {
        this.context = context;
        this.bilanganPrimaModelList = bilanganPrimaModelList;
    }

    @NonNull
    @Override
    public BilanganPrimaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_bilangan_prima, parent, false);
        return new BilanganPrimaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BilanganPrimaHolder holder, int position) {

        holder.bilangan_prima.setText(bilanganPrimaModelList.get(position).getBilangan_prima());
        holder.card_bilangan_prima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("bilangan_prima", bilanganPrimaModelList.get(position).getBilangan_prima());
                ((BilanganPrima) context).setResult(RESULT_OK, intent);
                ((BilanganPrima) context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return bilanganPrimaModelList.size();
    }

    class BilanganPrimaHolder extends RecyclerView.ViewHolder{

        CardView card_bilangan_prima;
        AppCompatTextView bilangan_prima;

        public BilanganPrimaHolder(@NonNull View itemView) {
            super(itemView);

            card_bilangan_prima = (CardView) itemView.findViewById(R.id.card_bilangan_prima);
            bilangan_prima = (AppCompatTextView) itemView.findViewById(R.id.bilangan_prima);
        }
    }
}
