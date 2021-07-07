package com.example.teste.ui.BancoDeDados.contatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.teste.ui.BancoDeDados.DbGateway;
import com.example.teste.ui.BancoDeDados.usuarios.Usuarios;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContatosDAO {
    private DbGateway gw;

    private static final String DB_TABLE = "DbContatos";

    public ContatosDAO(Context context){
        gw = DbGateway.getInstance(context);
    }

    public List<Contatos> viewData(String raFilter, int statusFilter){
        List<Contatos> contacts = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+DB_TABLE+" WHERE RA = '" + raFilter + "' AND Status = " + statusFilter;
        Cursor cursor = gw.getDatabase().rawQuery(selectQuery, null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex("Id"));
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
            String email = cursor.getString(cursor.getColumnIndex("Email"));

            byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

            int status = cursor.getInt(cursor.getColumnIndex("Status"));
            contacts.add(new Contatos(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook,
                    twitter, instagram, email, status, foto));
        }

        cursor.close();
        return contacts;
    }

    public Contatos buscarDadosContatoById(long idContact){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" WHERE Id = " + idContact, null);

        System.out.println(idContact);

        if(cursor.moveToFirst()) {

            long id = cursor.getInt(cursor.getColumnIndex("Id"));
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
            int status = cursor.getInt(cursor.getColumnIndex("Status"));

            byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

            return new Contatos(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook, twitter, instagram, emailUser, status, foto);

        }

        return null;
    }

    public Contatos buscarDadosContatoByIdAndStatus(long idContact, long raContact, int statusContact){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" WHERE Id = " + idContact + " AND Status = " + statusContact + " AND RA = " + raContact, null);

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
            int status = cursor.getInt(cursor.getColumnIndex("Status"));

            byte[] foto = cursor.getBlob(cursor.getColumnIndex("Foto"));

            return new Contatos(id, ra, nome, apelido, celular, whatsapp, telegram, nascimento, facebook, twitter, instagram, emailUser, status, foto);

        }

        return null;
    }

    public boolean gravar(String nome, String ra, String apelido, String celular, String whatsapp, String telegram,
                          String nascimento, String facebook, String twitter, String instagram,
                          String email, byte[] foto){

        ContentValues cv = new ContentValues();

        Date date = new Date();
        long newIdMethod = date.getTime();

        cv.put("Id", newIdMethod);
        cv.put("Ra", ra);
        cv.put("Nome", nome);
        cv.put("Apelido", apelido);
        if(celular.equals("")) cv.put("Celular", (Long) null);
        else cv.put("Celular", celular);

        if(celular.equals("")) cv.put("Whatsapp", (Long) null);
        else cv.put("Whatsapp", whatsapp);

        if(celular.equals("")) cv.put("Telegram", (Long) null);
        else cv.put("Telegram", telegram);

        String nascimentoTemp = nascimento;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dataEntrada = sdf.parse(nascimentoTemp);//Este método lança ParseException, colocar dentro de try/catch
            String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(dataEntrada);

            cv.put("Nascimento", dataFormatada);

        } catch (Exception e){
            System.out.println("Error: "+ e);
        }

        cv.put("Facebook", facebook);
        cv.put("Twitter", twitter);
        cv.put("Instagram", instagram);
        cv.put("Email", email);
        cv.put("Foto", foto);

        return gw.getDatabase().insert(DB_TABLE, null, cv) > 0;
    }

    public boolean gravar(long id, String nome, long ra, String apelido, String celular, String whatsapp, String telegram,
                          String nascimento, String facebook, String twitter, String instagram,
                          String email, byte[] foto){

        ContentValues cv = new ContentValues();

        cv.put("Id", id);
        cv.put("Ra", ra);
        cv.put("Nome", nome);
        cv.put("Apelido", apelido);
        if(celular.equals("")) cv.put("Celular", (Long) null);
        else cv.put("Celular", celular);

        if(celular.equals("")) cv.put("Whatsapp", (Long) null);
        else cv.put("Whatsapp", whatsapp);

        if(celular.equals("")) cv.put("Telegram", (Long) null);
        else cv.put("Telegram", telegram);

        String nascimentoTemp = nascimento;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date dataEntrada = sdf.parse(nascimentoTemp);//Este método lança ParseException, colocar dentro de try/catch
            String dataFormatada = new SimpleDateFormat("yyyy-MM-dd").format(dataEntrada);

            cv.put("Nascimento", dataFormatada);

        } catch (Exception e){
            System.out.println("Error: "+ e);
        }

        cv.put("Facebook", facebook);
        cv.put("Twitter", twitter);
        cv.put("Instagram", instagram);
        cv.put("Email", email);
        cv.put("Foto", foto);

        return gw.getDatabase().insert(DB_TABLE, null, cv) > 0;
    }

    public boolean deletar(long id){
        ContentValues cv = new ContentValues();

        cv.put("Status", 1);

        return gw.getDatabase().update(DB_TABLE, cv, "Id=?", new String[] {Long.toString((id))}) > 0;
//        return gw.getDatabase().delete(DB_TABLE, "Id=?", new String[] {Integer.toString(id)}) > 0;
    }

    public boolean apagar(long id){
        return gw.getDatabase().delete(DB_TABLE, "Id=?", new String[] {Long.toString(id)}) > 0;
    }

    public boolean update(long id, String nome, String apelido, String celular, String whatsapp, String telegram,
                          String nascimento, String facebook, String twitter, String instagram, String email, byte[] foto){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);
        cv.put("Apelido", apelido);
        cv.put("Celular", celular);
        cv.put("Whatsapp", whatsapp);
        cv.put("Telegram", telegram);
        cv.put("Nascimento", nascimento);
        cv.put("Facebook", facebook);
        cv.put("Twitter", twitter);
        cv.put("Instagram", instagram);
        cv.put("Email", email);
        cv.put("Foto", foto);

        return gw.getDatabase().update(DB_TABLE, cv, "Id=?", new String[] {Long.toString((id))}) > 0;
    }

    public boolean recuperarContato(long id){
        ContentValues cv = new ContentValues();

        cv.put("Status", 0);

        return gw.getDatabase().update(DB_TABLE, cv, "Id=?", new String[] {Long.toString((id))}) > 0;
    }

    public String retornarUltimaInclusao(){
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM "+DB_TABLE+" ORDER BY Id DESC", null);
        if(cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndex("Id"));
            cursor.close();
            return Long.toString(id);
        }
        return "";
    }

    public boolean updateStatus(long id, long ra, int status){
        ContentValues cv = new ContentValues();

        cv.put("RA", ra);
        cv.put("Status", status);

        return gw.getDatabase().update(DB_TABLE, cv, "Id=?", new String[] {Long.toString((id))}) > 0;
    }
}
