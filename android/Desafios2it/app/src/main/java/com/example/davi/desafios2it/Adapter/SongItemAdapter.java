package com.example.davi.desafios2it.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.davi.desafios2it.Model.Song;
import com.example.davi.desafios2it.R;

import java.util.ArrayList;

/**
 * Created by Davi on 15/09/2017.
 */

public class SongItemAdapter extends RecyclerView.Adapter<SongItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Song> songs;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title, detail;
        public ImageView image;
        public ViewHolder(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.title);
            detail = (TextView) v.findViewById(R.id.detail);
            image = (ImageView) v.findViewById(R.id.image);
        }
    }

    public SongItemAdapter(Context context, ArrayList<Song> myDataset) {
        this.context = context;
        songs = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        SongItemAdapter.ViewHolder  vh = new SongItemAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.title.setText(songs.get(position).getTrackName());
        holder.detail.setText(songs.get(position).getTrackName());
        Glide.with(context).load(songs.get(position).getArtworkUrl30()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

}