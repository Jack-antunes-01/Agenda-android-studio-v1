package com.example.teste.ui.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsHolder> {
    Activity context;
    private final List<Contatos> contatosList;

    @Override
    public ContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contatos_lista, parent, false);
        final ContactsHolder holder = new ContactsHolder(view);

        holder.lineData.setOnClickListener(v -> {
            Contatos c = contatosList.get(holder.getAdapterPosition());
            long id = c.getId();
//            byte[] foto = c.getFoto();

            HomeFragmentDirections.NavigateToContactsProfile action = HomeFragmentDirections.navigateToContactsProfile(
                   id
            );
            Navigation.findNavController(view).navigate(action);
        });

        return holder;
    }

    public ContactsAdapter(List<Contatos> contatos){
        this.contatosList = contatos;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsHolder holder, int position) {

        if(contatosList.get(position).getFoto() != null && contatosList.get(position).getFoto().length > 0){
            byte[] foto = contatosList.get(position).getFoto();
            Bitmap btm = BitmapFactory.decodeByteArray(foto , 0, foto.length);

            holder.list_item_image.setImageBitmap(btm);
        } else {
            holder.list_item_image.setImageResource(R.mipmap.ic_user);
        }

        holder.list_item_text.setText(contatosList.get(position).getNome());

    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return contatosList != null ? contatosList.size() : 0;
    }

    public void inserirUltimo(Contatos contatos) {
        contatosList.add(contatos);
        notifyItemInserted(getItemCount());
    }

    private Activity getActivity(View view) {
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) (ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public void atualizaAgenda(Contatos contatos) {
        contatosList.set(contatosList.indexOf(contatos), contatos);
        notifyItemInserted(contatosList.indexOf(contatosList));
    }

    public void removerAgenda(Contatos agenda) {
        int position = contatosList.indexOf(agenda);
        contatosList.remove(position);
        notifyItemRemoved(position);
    }

}
