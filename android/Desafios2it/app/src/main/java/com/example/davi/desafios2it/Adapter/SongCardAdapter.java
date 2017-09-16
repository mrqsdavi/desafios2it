package com.example.davi.desafios2it.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.davi.desafios2it.Model.Song;
import com.example.davi.desafios2it.R;

/**
 * Created by Davi on 15/09/2017.
 */

public class SongCardAdapter extends ArrayAdapter<Song> {

    public SongCardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_song_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        Song song = getItem(position);

        holder.title.setText(song.getTrackName());
        holder.detail.setText(song.getArtistName());
        Glide.with(getContext()).load(song.getArtworkUrl100().replace("100x100","300x300")).into(holder.image);

        return contentView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView detail;
        public ImageView image;

        public ViewHolder(View view) {
            this.title = (TextView) view.findViewById(R.id.title);
            this.detail = (TextView) view.findViewById(R.id.detail);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }

}
