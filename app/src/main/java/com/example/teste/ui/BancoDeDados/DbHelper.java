package com.example.teste.ui.BancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyDiary.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DB_TABLE_CONTACTS = "DbContatos";
    private static final String DB_TABLE_USERS = "DbUsuario";
    private final String CREATE_TABLE_CONTACTS = "CREATE TABLE "+DB_TABLE_CONTACTS+" (" +
            "Id BIGINT(15) NOT NULL, " +
            "RA BIGINT(15) NOT NULL, " +
            "Nome CHAR(50) NOT NULL, " +
            "Apelido CHAR(50)," +
            "Celular BIGINT(11) NOT NULL," +
            "Whatsapp BIGINT(11)," +
            "Telegram BIGINT(11)," +
            "Nascimento DATE," +
            "Facebook CHAR(100)," +
            "Twitter CHAR(100)," +
            "Instagram CHAR(100)," +
            "Email CHAR(100)," +
            "Foto BLOB," +
            "Status int(2) NOT NULL DEFAULT 0," +
            "PRIMARY KEY (Id, RA)" +
            ");";
    private final String CREATE_TABLE_USERS = "CREATE TABLE "+DB_TABLE_USERS+" (" +
            "Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "RA BIGINT(15) NOT NULL, " +
            "Nome CHAR(50) NOT NULL, " +
            "Apelido CHAR(100)," +
            "Celular BIGINT(11)," +
            "Whatsapp BIGINT(11)," +
            "Telegram BIGINT(11)," +
            "Nascimento DATE," +
            "Facebook CHAR(100)," +
            "Twitter CHAR(100)," +
            "Instagram CHAR(100)," +
            "Email CHAR(100) NOT NULL," +
            "Foto BLOB," +
            "Senha CHAR(30) NOT NULL" +
            ");";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
    }

}
