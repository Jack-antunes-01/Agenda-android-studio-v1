package com.example.teste.ui.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;

public class ContactsHolder extends RecyclerView.ViewHolder {
    public LinearLayout lineData;
    public ImageView list_item_image;
    public TextView list_item_text;

    public ContactsHolder(View itemView){
        super(itemView);
        lineData = itemView.findViewById(R.id.lineData);
        list_item_image = itemView.findViewById(R.id.list_item_image);
        list_item_text = itemView.findViewById(R.id.list_item_text);

    }
}
