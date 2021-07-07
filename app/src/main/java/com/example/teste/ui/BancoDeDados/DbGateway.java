 package com.example.teste.ui.BancoDeDados;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

 public class DbGateway {
     private static com.example.teste.ui.BancoDeDados.DbGateway gw;
     private SQLiteDatabase db;

     public DbGateway(Context context){
         com.example.teste.ui.BancoDeDados.DbHelper helper = new com.example.teste.ui.BancoDeDados.DbHelper(context);
         db = helper.getWritableDatabase();
     }

     public static com.example.teste.ui.BancoDeDados.DbGateway getInstance(Context context){
         if(gw == null){
             gw = new com.example.teste.ui.BancoDeDados.DbGateway(context);
         }
         return gw;
     }

     public SQLiteDatabase getDatabase(){
         return this.db;
     }
 }
