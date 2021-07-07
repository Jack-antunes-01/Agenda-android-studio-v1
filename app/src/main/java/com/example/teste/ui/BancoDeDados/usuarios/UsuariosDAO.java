package com.example.teste.ui.BancoDeDados.usuarios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.teste.ui.BancoDeDados.DbGateway;
import com.example.teste.ui.BancoDeDados.contatos.Contatos;
import com.lambdapioneer.argon2kt.Argon2Kt;
import com.lambdapioneer.argon2kt.Argon2KtResult;
import com.lambdapioneer.argon2kt.Argon2Mode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsuariosDAO {

    private DbGateway gw;

    private static final String DB_TABLE = "DbUsuario";
    private static final String DB_TABLE_CONTATOS = "DbContatos";

    public UsuariosDAO(Context context){
        gw = DbGateway.getInstance(context);
    }

    public Usuarios buscarDadosUsuarioById(int idUser){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" WHERE Id = " + idUser, null);

        if(cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            long ra = cursor.getLong(cursor.getColumnIndex("RA"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String apelido = cursor.getString(cursor.getColumnIndex("Apelido"));
            long celular = cursor.getLong(cursor.getColumnIndex("Celular"));
            long whatsapp = cursor.getLong(cursor.getColumnIndex("Whatsapp"));
            long telegram = cursor.getLong(cursor.getColumnIndex("Telegram"));
            String nascimento = cursor.getString(cursor.getColumnIndex("Nascimento"));
            String facebook = cursor.getString(cursor.getColumnIndex("Facebook"));
            String twitter = cursor.getString(cursor.getColumnIndex("Twitter"));
            String instagram = cursor.getString(cursor.getColumnIndex("Instagram"));
            String emailUser = cursor.getString(cursor.getColumnIndex("Email"));
            String senhaUser = cursor.getString(cursor.getColumnIndex("Senha"));
            byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

            return new Usuarios(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook, twitter, instagram, emailUser, senhaUser, foto);

        }

        return null;
    }

    public Usuarios buscarDadosUsuarioByRa(String raUser){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" WHERE RA = '" + raUser + "'", null);

        if(cursor.moveToFirst()) {

            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            long ra = cursor.getLong(cursor.getColumnIndex("RA"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String apelido = cursor.getString(cursor.getColumnIndex("Apelido"));
            long celular = cursor.getLong(cursor.getColumnIndex("Celular"));
            long whatsapp = cursor.getLong(cursor.getColumnIndex("Whatsapp"));
            long telegram = cursor.getLong(cursor.getColumnIndex("Telegram"));
            String nascimento = cursor.getString(cursor.getColumnIndex("Nascimento"));
            String facebook = cursor.getString(cursor.getColumnIndex("Facebook"));
            String twitter = cursor.getString(cursor.getColumnIndex("Twitter"));
            String instagram = cursor.getString(cursor.getColumnIndex("Instagram"));
            String emailUser = cursor.getString(cursor.getColumnIndex("Email"));
            String senhaUser = cursor.getString(cursor.getColumnIndex("Senha"));
            byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

            return new Usuarios(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook, twitter, instagram, emailUser, senhaUser, foto);

        }

        return null;
    }

    public boolean gravar(String nome, String email, long ra, String celular, String senha){

        String salt = "batatata";

        final Argon2Kt argon2Kt = new Argon2Kt();

        final Argon2KtResult hashResult = argon2Kt.hash(Argon2Mode.ARGON2_I, senha.getBytes(), salt.getBytes());
        final String encodedOutput = hashResult.encodedOutputAsString();

        final boolean verificationResult = argon2Kt.verify(Argon2Mode.ARGON2_I, encodedOutput, senha.getBytes());

        if(verificationResult) {

            ContentValues cv = new ContentValues();

            cv.put("Ra", ra);
            cv.put("Nome", nome);
            cv.put("Email", email);
            cv.put("Celular", celular);
            cv.put("Senha", encodedOutput);

            return gw.getDatabase().insert(DB_TABLE, null, cv) > 0;
        }

        return false;
    }

    public boolean gravarFacebook(String nome, String email, long ra, String senha, byte[] foto){

        String salt = "batatata";

        final Argon2Kt argon2Kt = new Argon2Kt();

        final Argon2KtResult hashResult = argon2Kt.hash(Argon2Mode.ARGON2_I, senha.getBytes(), salt.getBytes());
        final String encodedOutput = hashResult.encodedOutputAsString();

        final boolean verificationResult = argon2Kt.verify(Argon2Mode.ARGON2_I, encodedOutput, senha.getBytes());

        if(verificationResult) {

            ContentValues cv = new ContentValues();

            cv.put("Ra", ra);
            cv.put("Nome", nome);
            cv.put("Email", email);
            cv.put("Senha", encodedOutput);
            cv.put("Foto", foto);

            return gw.getDatabase().insert(DB_TABLE, null, cv) > 0;
        }

        return false;
    }


    public Usuarios logar(String email, String senha){

        String salt = "batatata";

        final Argon2Kt argon2Kt = new Argon2Kt();

        final Argon2KtResult hashResult = argon2Kt.hash(Argon2Mode.ARGON2_I, senha.getBytes(), salt.getBytes());
        final String encodedOutput = hashResult.encodedOutputAsString();

        final boolean verificationResult = argon2Kt.verify(Argon2Mode.ARGON2_I, encodedOutput, senha.getBytes());

        if(verificationResult) {
            Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM " + DB_TABLE + " WHERE Email = '" + email + "' AND Senha = '" + encodedOutput + "'", null);

            if (cursor.moveToFirst()) {

                int id = cursor.getInt(cursor.getColumnIndex("Id"));
                long ra = cursor.getLong(cursor.getColumnIndex("RA"));
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String apelido = cursor.getString(cursor.getColumnIndex("Apelido"));
                long celular = cursor.getLong(cursor.getColumnIndex("Celular"));
                long whatsapp = cursor.getLong(cursor.getColumnIndex("Whatsapp"));
                long telegram = cursor.getLong(cursor.getColumnIndex("Telegram"));
                String nascimento = cursor.getString(cursor.getColumnIndex("Nascimento"));
                String facebook = cursor.getString(cursor.getColumnIndex("Facebook"));
                String twitter = cursor.getString(cursor.getColumnIndex("Twitter"));
                String instagram = cursor.getString(cursor.getColumnIndex("Instagram"));
                String emailUser = cursor.getString(cursor.getColumnIndex("Email"));
                String senhaUser = cursor.getString(cursor.getColumnIndex("Senha"));
                byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

                return new Usuarios(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook, twitter, instagram, emailUser, senhaUser, foto);

            }
        }

        return null;
    }

    public boolean deletar(int id){
        return gw.getDatabase().delete(DB_TABLE, "Id=?", new String[] {Integer.toString(id)}) > 0;
    }

    public boolean update(int id, String nome, String apelido, String celular, String whatsapp, String telegram,
                          String nascimento, String facebook, String twitter, String instagram, byte[] img){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);
        cv.put("Apelido", apelido);
        cv.put("Celular", celular);
        cv.put("Whatsapp", whatsapp);
        cv.put("Nascimento", nascimento);
        cv.put("Telegram", telegram);
        cv.put("Facebook", facebook);
        cv.put("Twitter", twitter);
        cv.put("Instagram", instagram);
        cv.put("Celular", celular);
        cv.put("Foto", img);

        return gw.getDatabase().update(DB_TABLE, cv, "Id=?", new String[] {Integer.toString((id))}) > 0;
    }

    public boolean verificaRaRegistro(String ra){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM " + DB_TABLE + " WHERE RA = '" + ra + "'", null);

        if (cursor.moveToFirst()) {
            return true;
        }

        return cursor.moveToFirst();
    }

    public boolean verificaEmailRegistro(String email){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM " + DB_TABLE + " WHERE Email = '" + email + "'", null);

        if (cursor.moveToFirst()) {
            return true;
        }

        return false;
    }

    public String retornarUltimaInclusao(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" ORDER BY Id DESC", null);
        if(cursor.moveToFirst()){
            int id = cursor.getInt(cursor.getColumnIndex("Id"));
            cursor.close();
            return Integer.toString(id);
        }
        return "";
    }

}
