package com.example.teste.ui.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;

public class RecuperableDataHolder extends RecyclerView.ViewHolder {

    public Button recuperarContatoMySql;
    public TextView nomeRecuperarContatoMySql;
    public ImageView imagemRecuperarContatoMySql;

    public RecuperableDataHolder(View itemView){
        super(itemView);
        recuperarContatoMySql = itemView.findViewById(R.id.recuperarContatoMySql);
        nomeRecuperarContatoMySql = itemView.findViewById(R.id.nomeRecuperarContatoMySql);
        imagemRecuperarContatoMySql = itemView.findViewById(R.id.imagemRecuperarContatoMySql);
    }
}
