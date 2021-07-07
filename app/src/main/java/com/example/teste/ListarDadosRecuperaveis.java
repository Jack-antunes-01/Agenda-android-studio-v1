package com.example.teste;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.teste.ui.Adapter.ContactsAdapter;
import com.example.teste.ui.Adapter.ExcludedPeopleAdapter;
import com.example.teste.ui.Adapter.RecuperableDataAdapter;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragment;
import com.example.teste.ui.FragmentoInicial.HomeFragmentArgs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListarDadosRecuperaveis extends Fragment {

    private static final String url = "YOUR_URL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_recuperable_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Dados recuperáveis");

        if(getArguments() != null){

//            ListarDadosRecuperaveisArgs args = ListarDadosRecuperaveisArgs.fromBundle(getArguments());
//            String ra = args.getRa();
//            configuraRecycler(view, ra);

            new FindInformations().execute();

            FloatingActionButton btnBack = view.findViewById(R.id.fabBackRecuperableData);
            btnBack.setOnClickListener(v -> {
                Navigation.findNavController(view).popBackStack();
            });

        }




    }

    class FindInformations extends AsyncTask<Void, Void, Void> {

        RecyclerView recyclerView;
        RecuperableDataAdapter adapter;
        List<Contatos> contacts = new ArrayList<>();
        ProgressDialog progress = new ProgressDialog(getView().getContext());
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progress.setTitle("Carregando");
            progress.setMessage("Buscando contatos..");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... voids){

            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }
            try {
                recyclerView = getView().findViewById(R.id.recuperableDataList);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
                recyclerView.setLayoutManager(layoutManager);

                ListarDadosRecuperaveisArgs args = ListarDadosRecuperaveisArgs.fromBundle(getArguments());
                String ra = args.getRa();

                ContatosDAO dao = new ContatosDAO(getView().getContext());

                Contatos dadoContato;
                Contatos dadoContatoStatus1;

                String usuarioDb = "dreco836_amigos";
                String senhaDb = "amigos2021";
                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, usuarioDb, senhaDb);
                try {
                    Statement st = con.createStatement();
//                    ResultSet rs = st.executeQuery("SELECT * FROM Agenda WHERE RA = " + ra + " AND Status = 1" );
                    ResultSet rs = st.executeQuery("SELECT * FROM Agenda WHERE RA = " + ra );
                    while(rs.next()) {
                        System.out.println("O contato [" + rs.getString("Nome") + "] foi encontrado no banco.");

                        dadoContato = dao.buscarDadosContatoByIdAndStatus(rs.getLong("Id"), rs.getLong("RA"), 0);
                        dadoContatoStatus1 = dao.buscarDadosContatoByIdAndStatus(rs.getLong("Id"), rs.getLong("RA"), 1);

                        long idMySql = rs.getLong("Id");
                        long raMySql = rs.getLong("RA");
                        String nomeMySql = rs.getString("Nome");
                        String apelidoMySql = rs.getString("Apelido");
                        long celularMySql = rs.getLong("Celular");
                        long whatsappMySql = rs.getLong("Whatsapp");
                        long telegramMySql = rs.getLong("Telegram");
                        String nascimentoMySql = String.valueOf(rs.getDate("Nascimento"));
                        String facebookMySql = rs.getString("Facebook");
                        String twitterMySql = rs.getString("Twitter");
                        String instagramMySql = rs.getString("Instagram");
                        String emailMySql = rs.getString("Email");
                        int statusMySql = rs.getInt("Status");

                        byte[] fotoMySql = rs.getBytes("Foto");

                        if(!nascimentoMySql.equals("null")) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date dataEntrada = sdf.parse(nascimentoMySql);//Este método lança ParseException, colocar dentro de try/catch
                                nascimentoMySql = new SimpleDateFormat("dd/MM/yyyy").format(dataEntrada);
                            } catch (Exception e) {
                                System.out.println("Error: " + e);
                            }
                        }

                        if(dadoContato == null && dadoContatoStatus1 == null){
                            contacts.add(new Contatos(idMySql, raMySql, nomeMySql, apelidoMySql, celularMySql, whatsappMySql, telegramMySql,
                                    nascimentoMySql, facebookMySql, twitterMySql, instagramMySql, emailMySql, statusMySql, fotoMySql));
                        }
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
            progress.dismiss();

            if(contacts.isEmpty()){
                Button btnRecuperarTodosOsContatos = getView().findViewById(R.id.btnRecuperarTodosOsContatos);
                btnRecuperarTodosOsContatos.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
                builder.setTitle("Recuperar contatos")
                        .setMessage("Nenhum contato foi encontrado")
                        .setPositiveButton("Ok", null)
                        .create()
                        .show();
            } else {
                adapter = new RecuperableDataAdapter(contacts);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getView().getContext(), DividerItemDecoration.VERTICAL));
            }
        }

    }

}
