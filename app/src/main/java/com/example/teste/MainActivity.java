package com.example.teste;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    private static Context context;

    private AppBarConfiguration mAppBarConfiguration;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        MainActivity.context = getApplicationContext();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        DrawerLayout drawer = findViewById(R.id.drawer_layout);

//        NavigationView navigationView = findViewById(R.id.nav_view);

//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_profile)
//                .setDrawerLayout(drawer)
//                .build();
//
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

    }

//                HomeFragmentDirections.actionNavHomeToNavProfile();
//                HomeFragmentDirections.ActionNavHomeToNavProfile action = HomeFragmentDirections.actionNavHomeToNavProfile(
//                        dadosUsuario.getNome(), dadosUsuario.getApelido(), dadosUsuario.getNascimento(), dadosUsuario.getEmail(), Long.toString(dadosUsuario.getCelular()),
//                        Long.toString(dadosUsuario.getWhatsapp()), Long.toString(dadosUsuario.getTelegram()), dadosUsuario.getFacebook(), dadosUsuario.getTwitter(), dadosUsuario.getInstagram(),
//                        Integer.toString(dadosUsuario.getId())
//                );
//                Navigation.findNavController(view).navigate(action);


//    if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
//    }

//
//    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                builder.setTitle("Confirmação de exclusão de contato")
//                        .setMessage("Tem certeza de que deseja excluir o contato [" + dadosContato.getNome() + "] ?")
//            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            ContatosDAO dao = new ContatosDAO(view.getContext());
//            boolean sucesso = dao.deletar(id);
//
//            if(sucesso){
//                Navigation.findNavController(view).popBackStack();
//            } else {
//                Snackbar.make(view, "Houve falha ao deletar os dados de [" + dadosContato.getNome() + "].", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show();
//            }
//        }
//    })
//            .setNegativeButton("Não", null)
//                        .create()
//                        .show();
//
    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

//    public static Context getAppContext() {
//        return MainActivity.context;
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}