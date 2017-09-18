package com.example.davi.desafios2it.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.davi.desafios2it.Model.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Davi on 15/09/2017.
 */

public class DataSource {

    private Context context;
    private SQLiteDatabase db;
    private DataBaseHelper banco;


    public DataSource(Context context){
        this.context = context;
        banco = new DataBaseHelper(context);
    }

    public ArrayList<Song> getSongs(String urlString){

        ArrayList<Song> songs = null;
        JSONObject jsonObject = getJSONObject(urlString);

        Type listType = new TypeToken<ArrayList<Song>>(){}.getType();

        try {
            songs = new Gson().fromJson(jsonObject.getJSONArray("results").toString(), listType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(songs != null){
            long seed = System.nanoTime();
            Collections.shuffle(songs, new Random(seed));
        }



        return songs;
    }

    public boolean insertLike(Song song){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(DataBaseHelper.TITULO, song.getTrackName());
        valores.put(DataBaseHelper.ARTISTA, song.getArtistName());
        valores.put(DataBaseHelper.COVER, song.getArtworkUrl30());
        valores.put(DataBaseHelper.ID, song.getTrackId());

        resultado = db.insert(DataBaseHelper.TABELA, null, valores);
        db.close();

        if (resultado ==-1)
            return false;
        else
            return true;
    }

    public ArrayList<Song> getLikes(){
        Cursor cursor;
        String[] campos =  {DataBaseHelper.ID, DataBaseHelper.TITULO,DataBaseHelper.ARTISTA,DataBaseHelper.COVER};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

        ArrayList<Song> result = new ArrayList<>();

        if(cursor!=null){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Song song = new Song();
                song.setTrackId(cursor.getInt(0));
                song.setTrackName(cursor.getString(1));
                song.setArtistName(cursor.getString(2));
                song.setArtworkUrl30(cursor.getString(3));
                result.add(song);
                cursor.moveToNext();
            }
        }
        db.close();
        return result;
    }

    public JSONObject getJSONObject(String urlString) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(10000);
            connection.connect();

            InputStream input = connection.getInputStream();
            String jsonString = inputStreamToString(input);
            JSONObject jsonObject = new JSONObject(jsonString);
            return  jsonObject;
        }
        catch (Exception e) {}

        return null;

    }

    String inputStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        try
        {
            while ((line = reader.readLine()) != null)
            {
                stringBuilder.append(line);
            }
            is.close();
        }
        catch (IOException e)
        { }
        return stringBuilder.toString();
    }


}
