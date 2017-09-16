package com.example.davi.desafios2it.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.davi.desafios2it.Model.Song;

/**
 * Created by Davi on 15/09/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO = "banco.db";
    public static final String TABELA = "songs";
    public static final String ID = "trackId";
    public static final String TITULO = "trackName";
    public static final String ARTISTA = "artistName";
    public static final String COVER = "artworkUrl30";
    public static final int VERSAO = 8;

    public DataBaseHelper(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key, "
                + TITULO + " text, "
                + ARTISTA + " text, "
                + COVER + " text "
                +")";
        Log.v("DEV","SQL = "+sql);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
