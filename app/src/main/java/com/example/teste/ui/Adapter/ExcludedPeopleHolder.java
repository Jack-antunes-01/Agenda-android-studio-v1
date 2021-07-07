package com.example.teste.ui.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;

public class ExcludedPeopleHolder extends RecyclerView.ViewHolder {
    public Button recuperarContato;
    public TextView nomeRecuperarContato;
    public ImageView imagemRecuperarContato;

    public ExcludedPeopleHolder(View itemView){
        super(itemView);
        recuperarContato = itemView.findViewById(R.id.recuperarContato);
        nomeRecuperarContato = itemView.findViewById(R.id.nomeRecuperarContato);
        imagemRecuperarContato = itemView.findViewById(R.id.imagemRecuperarContato);
    }
}
