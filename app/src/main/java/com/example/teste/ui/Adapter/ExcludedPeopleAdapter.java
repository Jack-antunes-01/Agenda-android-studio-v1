package com.example.teste.ui.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;

import java.util.List;

public class ExcludedPeopleAdapter extends RecyclerView.Adapter<ExcludedPeopleHolder> {
    Activity context;
    private final List<Contatos> contatosList;

    @Override
    public ExcludedPeopleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_excluded_people, parent, false);
        final ExcludedPeopleHolder holder = new ExcludedPeopleHolder(view);

        holder.recuperarContato.setOnClickListener(v -> {
            Contatos c = contatosList.get(holder.getAdapterPosition());
            long id = c.getId();

            ContatosDAO dao = new ContatosDAO(holder.recuperarContato.getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Recuperar contato")
                    .setMessage("Deseja recuperar este contato?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        boolean successo = dao.recuperarContato(id);

                        if(successo){
                            Toast.makeText(holder.recuperarContato.getContext(), "Contato recuperado com sucesso", Toast.LENGTH_SHORT).show();
                            removerAgenda(c);
                        } else {
                            Toast.makeText(holder.recuperarContato.getContext(), "Falha ao recuperar o contato", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Não", null)
                    .create()
                    .show();
        });

        return holder;
    }

    public ExcludedPeopleAdapter(List<Contatos> contatos){
        this.contatosList = contatos;
    }

    @Override
    public void onBindViewHolder(@NonNull ExcludedPeopleHolder holder, int position) {

        if(contatosList.get(position).getFoto() != null && contatosList.get(position).getFoto().length > 0){
            byte[] foto = contatosList.get(position).getFoto();
            Bitmap btm = BitmapFactory.decodeByteArray(foto , 0, foto.length);

            holder.imagemRecuperarContato.setImageBitmap(btm);
        } else {
            holder.imagemRecuperarContato.setImageResource(R.mipmap.ic_user);
        }
        holder.nomeRecuperarContato.setText(contatosList.get(position).getNome());

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
