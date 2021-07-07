package com.example.teste.ui.TelaDeLogin;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.teste.MainActivity;
import com.example.teste.PrefUtil;
import com.example.teste.R;
import com.example.teste.ui.BancoDeDados.DbGateway;
import com.example.teste.ui.BancoDeDados.DbHelper;
import com.example.teste.ui.BancoDeDados.usuarios.Usuarios;
import com.example.teste.ui.BancoDeDados.usuarios.UsuariosDAO;
import com.example.teste.ui.Contatos.ContatosCadastroArgs;
import com.example.teste.ui.Contatos.ContatosPerfilDirections;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class LoginFragment extends Fragment {

    PrefUtil pref;
    CallbackManager callbackManager;

    private EditText eUsername, ePassword;
    private Button btnLogin, btnRegister;

    boolean isValid = false;

    private int counter = 5;
    private Object Tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.login_screen, container, false);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        UsuariosDAO dao = new UsuariosDAO(view.getContext());

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if(isLoggedIn){

            String idFacebook = Profile.getCurrentProfile().getId();

            Usuarios dadosUsuario = dao.buscarDadosUsuarioByRa(idFacebook);

            LoginFragmentDirections.ActionLoginFragmentToNavHome action = LoginFragmentDirections.actionLoginFragmentToNavHome(
                    Long.toString(dadosUsuario.getRa()), Integer.toString(dadosUsuario.getId())
            );

            Navigation.findNavController(view).navigate(action);
        } else {

            FacebookSdk.sdkInitialize(FacebookSdk.getApplicationContext());
            callbackManager = CallbackManager.Factory.create();

            LoginButton loginButton = view.findViewById(R.id.facebook_login);
            loginButton.setFragment(this);
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email"));


            eUsername = view.findViewById(R.id.eEmail);
            ePassword = view.findViewById(R.id.ePassword);
            btnLogin = view.findViewById(R.id.btnLogin);
            btnRegister = view.findViewById(R.id.btnRegister);

            btnRegister.setOnClickListener(v -> {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            });

            btnLogin.setOnClickListener(v -> {
                String inputName = eUsername.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(view.getContext(), "Preencha os campos", Toast.LENGTH_SHORT).show();
                } else {

                    Usuarios user = dao.logar(inputName, inputPassword);

                    isValid = false;

                    if (user != null) {

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        View viewActivity = getActivity().getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (viewActivity == null) {
                            viewActivity = new View(getActivity());
                        }
                        imm.hideSoftInputFromWindow(viewActivity.getWindowToken(), 0);

                        LoginFragmentDirections.ActionLoginFragmentToNavHome action = LoginFragmentDirections.actionLoginFragmentToNavHome(
                                Long.toString(user.getRa()), Integer.toString(user.getId())
                        );

                        Navigation.findNavController(view).navigate(action);

                    } else {
                        counter--;
                        Toast.makeText(view.getContext(), "Usuário e/ou senha inválidos", Toast.LENGTH_SHORT).show();

                        if (counter == 0) {
                            btnLogin.setEnabled(false);
                        }
                    }

                }

            });


            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                                    (jsonObject, response) -> {
                                        // Getting FB User Data
                                        Bundle facebookData = getFacebookData(jsonObject);

                                        String name = facebookData.getString("first_name") + " " + facebookData.getString("last_name");
                                        String email = facebookData.getString("email");
                                        String profile_pic = facebookData.getString("profile_pic");
                                        byte[] emptyArray = new byte[0];
                                        String idFacebook = facebookData.getString("idFacebook");

                                        Usuarios user = dao.logar(email, idFacebook);

                                        if (user != null) {
                                            LoginFragmentDirections.ActionLoginFragmentToNavHome action = LoginFragmentDirections.actionLoginFragmentToNavHome(
                                                    Long.toString(user.getRa()), Long.toString(user.getId())
                                            );

                                            Navigation.findNavController(view).navigate(action);
                                        } else {
                                            boolean sucesso = dao.gravarFacebook(name, email, Long.parseLong(idFacebook), idFacebook, null);

                                            if (sucesso) {

                                                String idUsuario = dao.retornarUltimaInclusao();

                                                LoginFragmentDirections.ActionLoginFragmentToNavHome action = LoginFragmentDirections.actionLoginFragmentToNavHome(
                                                        idFacebook, idUsuario
                                                );

                                                Navigation.findNavController(view).navigate(action);
                                            } else {
                                                Toast.makeText(view.getContext(), "Erro ao salvar dados no banco de dados", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,first_name,last_name,email,gender");
                            request.setParameters(parameters);
                            request.executeAsync();
                        }


                        @Override
                        public void onCancel() {
                            System.out.println("Login cancelado");
                        }

                        @Override
                        public void onError(FacebookException e) {
                            e.printStackTrace();
                            System.out.println("Login falhou");
                            deleteAccessToken();
                        }

                    }
            );
        }

    }

    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");
            URL profile_pic;
            try {
                profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));

             pref.saveFacebookUserInfo(object.getString("first_name"),
                    object.getString("last_name"),object.getString("email"),
                    object.getString("gender"), profile_pic.toString());

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        return bundle;
    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null){
                    //User logged out
                    pref.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}