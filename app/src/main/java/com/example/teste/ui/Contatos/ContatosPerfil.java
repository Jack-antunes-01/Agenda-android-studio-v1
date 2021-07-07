package com.example.teste.ui.Contatos;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavArgs;
import androidx.navigation.Navigation;

import com.example.teste.MainActivity;
import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.DbHelper;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.example.teste.ui.BancoDeDados.contatos.ContatosDAO;
import com.example.teste.ui.BancoDeDados.usuarios.Usuarios;
import com.example.teste.ui.BancoDeDados.usuarios.UsuariosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class ContatosPerfil extends Fragment {

    private static final int REQUEST_PHONE_CALL = 1;
    private static final int REQUEST_SMS = 2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contatos_perfil, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Perfil contato");

        if (getArguments() != null) {
            ContatosPerfilArgs args = ContatosPerfilArgs.fromBundle(getArguments());
            long id = args.getId();

            System.out.println("id: " + id);

            ContatosDAO contactsDao = new ContatosDAO(view.getContext());
            Contatos dadosContato = contactsDao.buscarDadosContatoById(id);


            TextView contactsName = view.findViewById(R.id.contactsName);
            ImageView contactsImageView = view.findViewById(R.id.contactsImageView);
            TextView contactsBirthDate = view.findViewById(R.id.contactsBirthDate);
            TextView contactsEmail = view.findViewById(R.id.contactsEmail);
            TextView contactsPhone = view.findViewById(R.id.contactsPhone);
            TextView contactsWhatsapp = view.findViewById(R.id.contactsWhatsapp);
            TextView contactsTelegram = view.findViewById(R.id.contactsTelegram);
            TextView contactsFacebook = view.findViewById(R.id.contactsFacebook);
            TextView contactsTwitter = view.findViewById(R.id.contactsTwitter);
            TextView contactsInstagram = view.findViewById(R.id.contactsInstagram);

            if(dadosContato.getApelido() != null && dadosContato.getApelido().length() > 0){
                contactsName.setText(dadosContato.getNome() + " (" + dadosContato.getApelido() + ")");
            } else {
                contactsName.setText(dadosContato.getNome());
            }

            if(dadosContato.getFoto() != null && dadosContato.getFoto().length > 0){
                Bitmap btm = BitmapFactory.decodeByteArray(dadosContato.getFoto(), 0, dadosContato.getFoto().length);

                contactsImageView.setImageBitmap(btm);
            } else {
                contactsImageView.setImageResource(R.mipmap.ic_user);
            }

            if (dadosContato.getNascimento() != null && dadosContato.getNascimento().length() > 0) {
                contactsBirthDate.setText(dadosContato.getNascimento());
            } else {
                contactsBirthDate.setText("Não cadastrado");
            }

            if (dadosContato.getEmail() != null && dadosContato.getEmail().length() > 0) {
                contactsEmail.setText(dadosContato.getEmail());
            } else {
                contactsEmail.setText("Não cadastrado");
            }

            if (Long.toString(dadosContato.getCelular()).length() > 1) {
                contactsPhone.setText(Long.toString(dadosContato.getCelular()));
            } else {
                contactsPhone.setText("Não cadastrado");
            }

            if (Long.toString(dadosContato.getWhatsapp()).length() > 1) {
                contactsWhatsapp.setText(Long.toString(dadosContato.getWhatsapp()));
            } else {
                contactsWhatsapp.setText("Não cadastrado");
            }

            if (Long.toString(dadosContato.getTelegram()).length() > 1) {
                contactsTelegram.setText(Long.toString(dadosContato.getTelegram()));
            } else {
                contactsTelegram.setText("Não cadastrado");
            }

            if (dadosContato.getFacebook() != null && dadosContato.getFacebook().length() > 0) {
                contactsFacebook.setText(dadosContato.getFacebook());
            } else {
                contactsFacebook.setText("Não cadastrado");
            }

            if (dadosContato.getTwitter() != null && dadosContato.getTwitter().length() > 0) {
                contactsTwitter.setText(dadosContato.getTwitter());
            } else {
                contactsTwitter.setText("Não cadastrado");
            }

            if (dadosContato.getInstagram() != null && dadosContato.getInstagram().length() > 0) {
                contactsInstagram.setText(dadosContato.getInstagram());
            } else {
                contactsInstagram.setText("Não cadastrado");
            }


            FloatingActionButton fabLigar = view.findViewById(R.id.fabLigar);
            fabLigar.setOnClickListener(v -> {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:55"+dadosContato.getCelular()));

                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                }
                else
                {
                    startActivity(callIntent);
                }

            });

            FloatingActionButton fabDeletar = view.findViewById(R.id.fabDeletar);
            fabDeletar.setOnClickListener(v -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Confirmação de exclusão de contato")
                        .setMessage("Tem certeza de que deseja excluir o contato [" + dadosContato.getNome() + "] ?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContatosDAO dao = new ContatosDAO(view.getContext());
                                boolean sucesso = dao.deletar(id);

                                if(sucesso){
                                    Navigation.findNavController(view).popBackStack();
                                } else {
                                    Snackbar.make(view, "Houve falha ao deletar os dados de [" + dadosContato.getNome() + "].", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null)
                                            .show();
                                }
                            }
                        })
                        .setNegativeButton("Não", null)
                        .create()
                        .show();

            });

            FloatingActionButton fabEdit = view.findViewById(R.id.fabEditar);
            fabEdit.setOnClickListener(v -> {
                ContatosPerfilDirections.ActionNavContactsProfileToNavAddContact action = ContatosPerfilDirections.actionNavContactsProfileToNavAddContact(
                        Long.toString(id), ""
                );
                Navigation.findNavController(view).navigate(action);
            });

            FloatingActionButton fabBackContactsProfile = view.findViewById(R.id.fabBackContactsProfile);
            fabBackContactsProfile.setOnClickListener(v -> {
                Navigation.findNavController(view).popBackStack();
            });

            FloatingActionButton fabEnviarEmail = view.findViewById(R.id.fabEnviarEmail);
            fabEnviarEmail.setOnClickListener(v -> {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Texto enviado através do app MyDiary");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            });

            ImageButton whatsappMessage = view.findViewById(R.id.whatsappMessage);
            whatsappMessage.setOnClickListener(v -> {
                String url = "https://api.whatsapp.com/send?phone=55"+dadosContato.getCelular();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            });

            ImageButton telegramMessage = view.findViewById(R.id.telegramMessage);
            telegramMessage.setOnClickListener(v -> {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Enviando mensagens no telegram a partir da MyDiary");
                intent.setType("text/html");
                intent.setPackage("org.telegram.messenger");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "Por favor instale o telegram", Toast.LENGTH_LONG).show();
                }
            });

            ImageButton smsMessage = view.findViewById(R.id.smsMessage);
            smsMessage.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:55" +dadosContato.getCelular()));
                intent.putExtra("sms_body", "Mensagem sendo enviada através do app: MyDiary !");

                if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},REQUEST_SMS);
                }
                else
                {
                    startActivity(intent);
                }
            });

            ImageButton messengerMessage = view.findViewById(R.id.messengerMessage);
            messengerMessage.setOnClickListener(v -> {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Oi, mensagem enviada através do app: MyDiary !");
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.facebook.orca");

                try {
                    startActivity(sendIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(view.getContext(), "Por favor instale o messenger", Toast.LENGTH_LONG).show();
                }
            });

            ImageButton gmailMessage = view.findViewById(R.id.gmailMessage);
            gmailMessage.setOnClickListener(v -> {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={dadosContato.getEmail()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"MyDiary...");
                intent.putExtra(Intent.EXTRA_TEXT,"Enviando email a partir da MyDiary");
//                intent.putExtra(Intent.EXTRA_CC,"mailcc@gmail.com");
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            });
        }

    }

}
