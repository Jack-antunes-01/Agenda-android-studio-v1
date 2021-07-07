package com.example.teste.ui.FragmentoInicial;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.teste.MainActivity;
import com.example.teste.R;
import com.example.teste.ui.Adapter.ContactsAdapter;
import com.example.teste.ui.BancoDeDados.DbHelper;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.BancoDeDados.usuarios.Usuarios;
import com.example.teste.ui.Contatos.ContatosPerfilDirections;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String url = "YOUR_URL";
    private static final int WIFI_STATE = 5;
    private static final int REQUEST_ENABLE_BT = 6;
    private ArrayList<Contatos> listItem = new ArrayList<>();
    HomeFragmentArgs args;
    Method method;

//    private TextView drawerName, drawerEmail;
//    private ImageView drawerImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null){

            args = HomeFragmentArgs.fromBundle(getArguments());

            String ra = args.getRa();
            String id = args.getId();

            configuraRecycler(view, ra);

            view.findViewById(R.id.fabProfile).setOnClickListener(v -> {
                HomeFragmentDirections.ActionNavHomeToNavProfile action = HomeFragmentDirections.actionNavHomeToNavProfile(id);
                Navigation.findNavController(view).navigate(action);
            });

            view.findViewById(R.id.fab).setOnClickListener(v -> {
                HomeFragmentDirections.NavigateToAddContacts action = HomeFragmentDirections.navigateToAddContacts(
                        "", ra
                );
                Navigation.findNavController(view).navigate(action);
            });

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("MyDiary");
    }

    RecyclerView recyclerView;
    ContactsAdapter adapter;
    public void configuraRecycler(View view, String ra){
        recyclerView = view.findViewById(R.id.users_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        ContatosDAO dao = new ContatosDAO(this.getContext());
        adapter = new ContactsAdapter(dao.viewData(ra, 0));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        args = HomeFragmentArgs.fromBundle(getArguments());
        String ra = args.getRa();
        switch (item.getItemId()) {
            case R.id.settingsListDeletedData:
                HomeFragmentDirections.ActionNavHomeToListExcludedPeople action = HomeFragmentDirections.actionNavHomeToListExcludedPeople(ra);
                Navigation.findNavController(getView()).navigate(action);
                break;

            case R.id.logout:

                LoginManager.getInstance().logOut();
                Navigation.findNavController(getView()).navigate(HomeFragmentDirections.actionNavHomeToLoginFragment());
                break;

            case R.id.settingsSyncData:

                if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_WIFI_STATE},WIFI_STATE);
                } else {
                    boolean wifi = checkWifiOnAndConnected();

                    if (!wifi) {
                        boolean mobileDataEnabled = false; // Assume disabled
                        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        try {
                            Class cmClass = Class.forName(cm.getClass().getName());
                            method = cmClass.getDeclaredMethod("getMobileDataEnabled");
                            method.setAccessible(true);
                            mobileDataEnabled = (Boolean)method.invoke(cm);

                        } catch (Exception e) {
                            Toast.makeText(this.getContext(), "Erro ao verificar dados móveis", Toast.LENGTH_SHORT).show();
                        }
                        if(!mobileDataEnabled) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("")
                                    .setMessage("Conecte uma rede WiFi ou ligue os dados móveis")
                                    .setPositiveButton("Ok", null)
                                    .create()
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("Consumo de dados")
                                    .setMessage("O WiFi não está conectado, a sincronização vai consumir sua banda de dados, tem certeza que quer continuar?")
                                    .setPositiveButton("Sim", (dialog, which) -> {
                                        new Task().execute();
                                    })
                                    .setNegativeButton("Não", null)
                                    .create()
                                    .show();
                        }
                    } else {
                        new Task().execute();
                    }
                }

                break;

            case R.id.settingsCleanExcludedPeople:
                ContatosDAO dao = new ContatosDAO(this.getContext());
                List<Contatos> dadosStatus2 = dao.viewData(ra, 2);

                for(Contatos value: dadosStatus2){
                    boolean sucesso = dao.deletar(value.getId());
                    if(sucesso){
                        System.out.println("Sucesso ao deletar o contato [" + value.getNome() + "] do SQLITE");
                    } else {
                        System.out.println("Erro ao deletar o contato [" + value.getNome() + "] do SQLITE");
                    }
                }

                List<Contatos> dadosStatus1 = dao.viewData(ra, 1);

                for(Contatos value: dadosStatus1){
                    boolean sucesso = dao.updateStatus(value.getId(), value.getRa(), 3);
                    if(sucesso){
                        System.out.println("Sucesso ao alterar o status do contato [" + value.getNome() + "] no SQLITE");
                    } else {
                        System.out.println("Erro ao alterar o status do contato [" + value.getNome() + "] no SQLITE");
                    }
                }

                Toast.makeText(getView().getContext(), "Contatos limpos com sucesso.", Toast.LENGTH_SHORT).show();

                break;

            case R.id.settingsListRecuperableData:

                if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_WIFI_STATE},WIFI_STATE);
                } else {
                    boolean wifi = checkWifiOnAndConnected();

                    if (!wifi) {
                        boolean mobileDataEnabled = false; // Assume disabled
                        ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        try {
                            Class cmClass = Class.forName(cm.getClass().getName());
                            method = cmClass.getDeclaredMethod("getMobileDataEnabled");
                            method.setAccessible(true);
                            mobileDataEnabled = (Boolean)method.invoke(cm);

                        } catch (Exception e) {
                            Toast.makeText(this.getContext(), "Erro ao verificar dados móveis", Toast.LENGTH_SHORT).show();
                        }
                        if(!mobileDataEnabled) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("")
                                    .setMessage("Conecte uma rede WiFi ou ligue os dados móveis")
                                    .setPositiveButton("Ok", null)
                                    .create()
                                    .show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                            builder.setTitle("Consumo de dados")
                                    .setMessage("O WiFi não está conectado, a busca de dados recuperáveis vai consumir sua banda de dados, tem certeza que quer continuar?")
                                    .setPositiveButton("Sim", (dialog, which) -> {
                                        HomeFragmentDirections.ActionNavHomeToListarDadosRecuperaveis action2 = HomeFragmentDirections.actionNavHomeToListarDadosRecuperaveis(ra);
                                        Navigation.findNavController(getView()).navigate(action2);
                                    })
                                    .setNegativeButton("Não", null)
                                    .create()
                                    .show();
                        }
                    } else {
                        HomeFragmentDirections.ActionNavHomeToListarDadosRecuperaveis action2 = HomeFragmentDirections.actionNavHomeToListarDadosRecuperaveis(ra);
                        Navigation.findNavController(getView()).navigate(action2);
                    }
                }
                break;

            case R.id.settingsTransferirContato:
                if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.BLUETOOTH},WIFI_STATE);
                } else {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (mBluetoothAdapter == null) {
                        Toast.makeText(this.getContext(), "Seu celular não suporta bluetooth", Toast.LENGTH_SHORT).show();
                    } else if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
                    } else {
                        // Bluetooth is enabled
                    }
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) this.getContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

            if( wifiInfo.getNetworkId() == -1 ){
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        }
        else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    class Task extends AsyncTask<Void, Void, Void> {

        ProgressDialog progress = new ProgressDialog(getView().getContext());
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress.setTitle("Sincronizando");
            progress.setMessage("Aguarde um momento..");
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
                args = HomeFragmentArgs.fromBundle(getArguments());
                String ra = args.getRa();
                ResultSet rs;
                String usuarioDb = "YOUR_USER";
                String senhaDb = "YOUR_PASSWORD";

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection(url, usuarioDb, senhaDb);
                try {
                    Statement st = con.createStatement();

                    ContatosDAO daoContato = new ContatosDAO(getView().getContext());

                    List<Contatos> dadosContatos = daoContato.viewData(ra, 0);

//                    int batata = st.executeUpdate("DELETE FROM Agenda WHERE RA = 115924590696430 AND Id != 1 AND nome != 'Sabrine'");
//
//                    System.out.println(batata);

                    rs = st.executeQuery("SELECT * FROM Agenda");
                    while(rs.next()){
                        long idTeste = rs.getLong("Id");
                        long raTeste = rs.getLong("RA");
                        String nomeTeste = rs.getString("Nome");
                        System.out.println("Id ["+idTeste+"] - RA ["+raTeste+"] - Nome ["+nomeTeste+"]");
                    }
                    rs.close();

                    for (Contatos value : dadosContatos)
                    {
                        rs = st.executeQuery("SELECT * FROM Agenda WHERE Id = " + value.getId() + " AND RA = " + value.getRa());

                        if(rs.next()) {
                            System.out.println("O contato [" + rs.getString("Nome") + "] foi encontrado no banco.");
                            String statusMySql = rs.getString("Status");
                            if(!statusMySql.equals(Integer.toString(value.getStatus()))){
                                int rsStatus = st.executeUpdate("UPDATE Agenda SET Status = " + value.getStatus() + " " +
                                        "WHERE Id = " + rs.getLong("Id") + " AND RA = " +rs.getLong("RA"));

                                System.out.println("Linhas afetadas: [" + rsStatus + "]\nExpectativa: 1");
                                if(rsStatus == 0){
                                    System.out.println("Erro ao atualizar o Status do contato: " + value.getNome());
                                } else {
                                    System.out.println("Sucesso ao atualizar o Status do contato: " + value.getNome());
                                }
                            } else {
                                System.out.println("Status coincide com o MySQL, não há alteração.");
                            }
                        } else {
                            String nasc;
                            if(value.getNascimento() == null || value.getNascimento().equals("")){
                                nasc = "0000-00-00";
                            } else {
                                nasc = value.getNascimento();
                            }

                            String query = "INSERT INTO Agenda (RA, Id, nome, Apelido, Celular, WhatsAPP, Telegram, Nascimento, Facebook, Twitter, " +
                                    "Instagram, Email, Foto, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement pstmt = con.prepareStatement(query);
                            pstmt.setLong(1, value.getRa());
                            pstmt.setLong(2, value.getId());
                            pstmt.setString(3, value.getNome());
                            pstmt.setString(4, value.getApelido());
                            pstmt.setLong(5, value.getCelular());
                            pstmt.setLong(6, value.getWhatsapp());
                            pstmt.setLong(7, value.getTelegram());
                            pstmt.setString(8, nasc);
                            pstmt.setString(9, value.getFacebook());
                            pstmt.setString(10, value.getTwitter());
                            pstmt.setString(11, value.getInstagram());
                            pstmt.setString(12, value.getEmail());
                            pstmt.setBytes(13, value.getFoto());
                            pstmt.setInt(14, value.getStatus());
                            pstmt.execute();
                        }

                        rs.close();
                    }

                    List<Contatos> dadosContatosStatus1 = daoContato.viewData(ra, 1);

                    for (Contatos value : dadosContatosStatus1)
                    {
                        rs = st.executeQuery("SELECT * FROM Agenda WHERE Id = " + value.getId() + " AND RA = " + value.getRa());

                        if(rs.next()) {
                            System.out.println("O contato excluído do SqLITE [" + rs.getString("Nome") + "] foi encontrado no banco.");

                            int rsStatus = st.executeUpdate("UPDATE Agenda SET Status = " + value.getStatus() + " " +
                                    "WHERE Id = " + rs.getLong("Id") + " AND RA = " +rs.getLong("RA"));

                            System.out.println("Linhas afetadas: [" + rsStatus + "]\nExpectativa: 1");
                            if(rsStatus == 0){
                                System.out.println("Erro ao atualizar o Status do contato: " + value.getNome());
                            } else {
                                System.out.println("Sucesso ao atualizar o Status do contato: " + value.getNome());
                                boolean sucesso = daoContato.updateStatus(value.getId(), value.getRa(), 2);

                                if (sucesso) {
                                    System.out.println("Sucesso ao alterar o status no SqLITE para 2");
                                } else {
                                    System.out.println("Erro ao alterar o status no SqLITE para 2");
                                }
                            }

                        } else {

                            String nasc;

                            if(value.getNascimento().equals("") || value.getNascimento() == null){
                                nasc = "0000-00-00";
                            } else {
                                nasc = value.getNascimento();
                            }

                            String query = "INSERT INTO Agenda (RA, Id, nome, Apelido, Celular, WhatsAPP, Telegram, Nascimento, Facebook, Twitter, " +
                                    "Instagram, Email, Foto, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement pstmt = con.prepareStatement(query);
                            pstmt.setLong(1, value.getRa());
                            pstmt.setLong(2, value.getId());
                            pstmt.setString(3, value.getNome());
                            pstmt.setString(4, value.getApelido());
                            pstmt.setLong(5, value.getCelular());
                            pstmt.setLong(6, value.getWhatsapp());
                            pstmt.setLong(7, value.getTelegram());
                            pstmt.setString(8, nasc);
                            pstmt.setString(9, value.getFacebook());
                            pstmt.setString(10, value.getTwitter());
                            pstmt.setString(11, value.getInstagram());
                            pstmt.setString(12, value.getEmail());
                            pstmt.setBytes(13, value.getFoto());
                            pstmt.setInt(14, value.getStatus());
                            pstmt.execute();

                            System.out.println("Sucesso ao inserir o contato: " + value.getNome());

                            boolean sucesso = daoContato.updateStatus(value.getId(), value.getRa(), 2);

                            if (sucesso) {
                                System.out.println("Sucesso ao alterar o status no SqLITE para 2");
                            } else {
                                System.out.println("Erro ao alterar o status no SqLITE para 2");
                            }

                        }

                        rs.close();
                    }

                    //Dados contatos status 2 não é considerado nesse processo.

                    List<Contatos> dadosContatosStatus3 = daoContato.viewData(ra, 3);
                    for (Contatos value : dadosContatosStatus3)
                    {
                        rs = st.executeQuery("SELECT * FROM Agenda WHERE Id = " + value.getId() + " AND RA = " + value.getRa());

                        if(rs.next()) {
                            System.out.println("O contato com status 3 do SqLITE [" + rs.getString("Nome") + "] foi encontrado no banco.");
                            String statusMySql = rs.getString("Status");

//                                ResultSet rsStatus = st.executeQuery("UPDATE Agenda SET " +
                            int rsStatus = st.executeUpdate("UPDATE Agenda SET Status = 1 " +
                                    "WHERE Id = " + rs.getLong("Id") + " AND RA = " +rs.getLong("RA"));

                            System.out.println("Linhas afetadas: [" + rsStatus + "]\nExpectativa: 1");
                            if(rsStatus == 0){
                                System.out.println("Erro ao atualizar o Status do contato: " + value.getNome());
                            } else {
                                System.out.println("Sucesso ao atualizar o Status do contato: " + value.getNome());
                                boolean sucesso = daoContato.apagar(value.getId());

                                if (sucesso) {
                                    System.out.println("Sucesso ao deletar o contato do SqLITE");
                                } else {
                                    System.out.println("Erro ao deletar o contato do SqLITE");
                                }
                            }
                        } else {
                            String nasc;

                            if(value.getNascimento() == null || value.getNascimento().equals("")){
                                nasc = "0000-00-00";
                            } else {
                                nasc = value.getNascimento();
                            }

                            String query = "INSERT INTO Agenda (RA, Id, nome, Apelido, Celular, WhatsAPP, Telegram, Nascimento, Facebook, Twitter, " +
                                    "Instagram, Email, Foto, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                            PreparedStatement pstmt = con.prepareStatement(query);
                            pstmt.setLong(1, value.getRa());
                            pstmt.setLong(2, value.getId());
                            pstmt.setString(3, value.getNome());
                            pstmt.setString(4, value.getApelido());
                            pstmt.setLong(5, value.getCelular());
                            pstmt.setLong(6, value.getWhatsapp());
                            pstmt.setLong(7, value.getTelegram());
                            pstmt.setString(8, nasc);
                            pstmt.setString(9, value.getFacebook());
                            pstmt.setString(10, value.getTwitter());
                            pstmt.setString(11, value.getInstagram());
                            pstmt.setString(12, value.getEmail());
                            pstmt.setBytes(13, value.getFoto());
                            pstmt.setInt(14, value.getStatus());
                            pstmt.execute();

                            System.out.println("Sucesso ao inserir o contato: " + value.getNome());
                            boolean sucesso = daoContato.apagar(value.getId());

                            if (sucesso) {
                                System.out.println("Sucesso ao deletar o contato do SqLITE");
                            } else {
                                System.out.println("Erro ao deletar o contato do SqLITE");
                            }
                        }

                        rs.close();
                    }

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

            AlertDialog.Builder builder = new AlertDialog.Builder(getView().getContext());
            builder.setTitle("Sincronização")
                    .setMessage("Contatos sincronizados com sucesso!")
                    .setPositiveButton("Ok", null)
                    .create()
                    .show();
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == REQUEST_ENABLE_BT) {  // Match the request code
            if (resultCode == getActivity().RESULT_OK) {
                Toast.makeText(this.getContext(), "Bluetooth ligado", Toast.LENGTH_LONG).show();
            } else {   // RESULT_CANCELED
                Toast.makeText(this.getContext(), "Erro ao ligar o bluetooth", Toast.LENGTH_LONG).show();
            }
        }
    }

}



