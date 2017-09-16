package com.example.davi.desafios2it;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.davi.desafios2it.Adapter.SongItemAdapter;
import com.example.davi.desafios2it.Model.Song;
import com.example.davi.desafios2it.Util.DataSource;

import java.util.ArrayList;

/**
 * Created by Davi on 15/09/2017.
 */

public class LikeFragment extends Fragment {

    View content;
    RecyclerView recyclerView;

    DataSource source;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        source = new DataSource(getContext());
        content = inflater.inflate(R.layout.fragment_like, container, false);

        setupView();

        return content;
    }

    public void setupView(){

        ArrayList<Song> songs = source.getLikes();

        Log.v("DEV", "Songs = "+ songs);

        recyclerView = (RecyclerView) content.findViewById(R.id.likes_recycler_view);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        SongItemAdapter mAdapter = new SongItemAdapter(getContext(), songs);
        recyclerView.setAdapter(mAdapter);
    }

}