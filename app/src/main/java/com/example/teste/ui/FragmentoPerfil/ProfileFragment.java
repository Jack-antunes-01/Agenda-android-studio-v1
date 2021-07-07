package com.example.teste.ui.FragmentoPerfil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.teste.MainActivity;
import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.usuarios.Usuarios;
import com.example.teste.ui.BancoDeDados.usuarios.UsuariosDAO;
import com.example.teste.ui.FragmentoInicial.HomeFragmentArgs;
import com.example.teste.ui.FragmentoInicial.HomeFragmentDirections;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class ProfileFragment extends Fragment {

    ProfilePictureView profilePictureView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle("Perfil pessoal");

        if(getArguments() != null){

            ProfileFragmentArgs args = ProfileFragmentArgs.fromBundle(getArguments());
            String id = args.getId();

            UsuariosDAO userDao = new UsuariosDAO(view.getContext());
            Usuarios dadosUsuario = userDao.buscarDadosUsuarioById(Integer.parseInt(id));

            TextView profileName = view.findViewById(R.id.profileName);
            ImageView profileImageView = view.findViewById(R.id.profileImageView);
            TextView profileBirthDate = view.findViewById(R.id.profileBirthDate);
            TextView profileEmail = view.findViewById(R.id.profileEmail);
            TextView profilePhone = view.findViewById(R.id.profilePhone);
            TextView profileWhatsapp = view.findViewById(R.id.profileWhatsapp);
            TextView profileTelegram = view.findViewById(R.id.profileTelegram);
            TextView profileFacebook = view.findViewById(R.id.profileFacebook);
            TextView profileTwitter = view.findViewById(R.id.profileTwitter);
            TextView profileInstagram = view.findViewById(R.id.profileInstagram);
            profilePictureView = view.findViewById(R.id.friendProfilePicture);

            if(dadosUsuario.getApelido() != null && dadosUsuario.getApelido().length() > 0){
                profileName.setText(dadosUsuario.getNome() + " (" + dadosUsuario.getApelido() + ")");
            } else {
                profileName.setText(dadosUsuario.getNome());
            }

            if(Long.toString(dadosUsuario.getRa()).length() > 7){

                if (dadosUsuario.getFoto() != null && dadosUsuario.getFoto().length > 0) {
                    Bitmap btm = BitmapFactory.decodeByteArray(dadosUsuario.getFoto(), 0, dadosUsuario.getFoto().length);
                    profileImageView.setImageBitmap(btm);

                    profilePictureView.setVisibility(View.GONE);
                    profileImageView.setVisibility(View.VISIBLE);
                } else {
                    profilePictureView.setProfileId(Long.toString(dadosUsuario.getRa()));

                    profilePictureView.setVisibility(View.VISIBLE);
                    profileImageView.setVisibility(View.GONE);
                }

            } else {
                if (dadosUsuario.getFoto() != null && dadosUsuario.getFoto().length > 0) {
                    Bitmap btm = BitmapFactory.decodeByteArray(dadosUsuario.getFoto(), 0, dadosUsuario.getFoto().length);
                    profileImageView.setImageBitmap(btm);
                } else {
                    profileImageView.setImageResource(R.mipmap.ic_user);
                }
                profilePictureView.setVisibility(View.GONE);
                profileImageView.setVisibility(View.VISIBLE);
            }

            if (dadosUsuario.getNascimento() != null && dadosUsuario.getNascimento().length() > 0) {
                profileBirthDate.setText(dadosUsuario.getNascimento());
            } else {
                profileBirthDate.setText("Não cadastrado");
            }

            if (dadosUsuario.getEmail() != null && dadosUsuario.getEmail().length() > 0) {
                profileEmail.setText(dadosUsuario.getEmail());
            } else {
                profileEmail.setText("Não cadastrado");
            }

            if (Long.toString(dadosUsuario.getCelular()).length() > 1) {
                profilePhone.setText(Long.toString(dadosUsuario.getCelular()));
            } else {
                profilePhone.setText("Não cadastrado");
            }

            if (Long.toString(dadosUsuario.getWhatsapp()).length() > 1) {
                profileWhatsapp.setText(Long.toString(dadosUsuario.getWhatsapp()));
            } else {
                profileWhatsapp.setText("Não cadastrado");
            }

            if (Long.toString(dadosUsuario.getTelegram()).length() > 1) {
                profileTelegram.setText(Long.toString(dadosUsuario.getTelegram()));
            } else {
                profileTelegram.setText("Não cadastrado");
            }

            if (dadosUsuario.getFacebook() != null && dadosUsuario.getFacebook().length() > 0) {
                profileFacebook.setText(dadosUsuario.getFacebook());
            } else {
                profileFacebook.setText("Não cadastrado");
            }

            if (dadosUsuario.getTwitter() != null && dadosUsuario.getTwitter().length() > 0) {
                profileTwitter.setText(dadosUsuario.getTwitter());
            } else {
                profileTwitter.setText("Não cadastrado");
            }

            if (dadosUsuario.getInstagram() != null && dadosUsuario.getInstagram().length() > 0) {
                profileInstagram.setText(dadosUsuario.getInstagram());
            } else {
                profileInstagram.setText("Não cadastrado");
            }

            view.findViewById(R.id.fabEditarPerfil).setOnClickListener(v -> {
                ProfileFragmentDirections.ActionNavProfileToPerfilEditar action = ProfileFragmentDirections.actionNavProfileToPerfilEditar(id);
                Navigation.findNavController(view).navigate(action);
            });

            view.findViewById(R.id.fabBackPerfil).setOnClickListener(v -> {
                Navigation.findNavController(view).popBackStack();
            });

        }

    }

}