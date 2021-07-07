package com.example.teste.ui.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teste.ListarDadosRecuperaveisArgs;
import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecuperableDataAdapter extends RecyclerView.Adapter<RecuperableDataHolder> {

    Activity context;
    private final List<Contatos> contatosList;
    private static final String url = "YOUR_URL";

    @Override
    public RecuperableDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recuperable_data, parent, false);
        final RecuperableDataHolder holder = new RecuperableDataHolder(view);

        holder.recuperarContatoMySql.setOnClickListener(v -> {
            Contatos c = contatosList.get(holder.getAdapterPosition());
            byte[] foto;

            long id = c.getId();
            long ra = c.getRa();
            String nome = c.getNome();
            String apelido = c.getApelido();
            long celular = c.getCelular();
            long whatsapp = c.getWhatsapp();
            long telegram = c.getTelegram();
            String nascimento = c.getNascimento();
            String facebook = c.getFacebook();
            String twitter = c.getTwitter();
            String instagram = c.getInstagram();
            String email = c.getEmail();
            foto = c.getFoto();

            if(foto.length == 0){
                foto = new byte[0];
            }

            ContatosDAO dao = new ContatosDAO(holder.recuperarContatoMySql.getContext());

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            byte[] finalFoto = foto;
            builder.setTitle("Recuperar contato")
                    .setMessage("Deseja recuperar este contato?")
                    .setPositiveButton("Sim", (dialog, which) -> {

                        Contatos dadosContato = dao.buscarDadosContatoByIdAndStatus(id, ra, 2);

                        if(dadosContato == null){
                            dao.gravar(id, nome, ra, apelido, Long.toString(celular),
                                Long.toString(whatsapp), Long.toString(telegram), nascimento, facebook, twitter,
                                instagram, email, finalFoto);
                        } else {
                            dao.updateStatus(id, ra, 0);
                        }
                        new AsynsTask().execute(id, ra);

                        removerAgenda(c);

                    })
                    .setNegativeButton("Não", null)
                    .create()
                    .show();
        });

        return holder;
    }

    public RecuperableDataAdapter(List<Contatos> contatos){
        this.contatosList = contatos;
    }

    @Override
    public void onBindViewHolder(@NonNull RecuperableDataHolder holder, int position) {

        if(contatosList.get(position).getFoto() != null && contatosList.get(position).getFoto().length > 0){
            byte[] foto = contatosList.get(position).getFoto();
            Bitmap btm = BitmapFactory.decodeByteArray(foto , 0, foto.length);

            holder.imagemRecuperarContatoMySql.setImageBitmap(btm);
        } else {
            holder.imagemRecuperarContatoMySql.setImageResource(R.mipmap.ic_user);
        }
        holder.nomeRecuperarContatoMySql.setText(contatosList.get(position).getNome());

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

    class AsynsTask extends AsyncTask<Long, Long, Void> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Long... longs){

            long id = longs[0];
            long ra = longs[1];

            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            try {
                String usuarioDb = "YOUR_USER";
                String senhaDb = "YOUR_PASSWORD";
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, usuarioDb, senhaDb);

                try {
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery("SELECT * FROM Agenda WHERE RA = " + ra + " AND Id = " + id);

                    if(rs.next()) {
                        System.out.println("O contato [" + rs.getString("Nome") + "] foi encontrado no banco.");

                        int rsStatus = st.executeUpdate("UPDATE Agenda SET Status = 0 " +
                                "WHERE Id = " + rs.getLong("Id") + " AND RA = " +rs.getLong("RA"));

                        System.out.println("Linhas afetadas: [" + rsStatus + "]\nExpectativa: 1");
                    }

                    rs.close();
                    st.close();
                    con.close();

                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            super.onPostExecute(aVoid);
        }

    }

}
