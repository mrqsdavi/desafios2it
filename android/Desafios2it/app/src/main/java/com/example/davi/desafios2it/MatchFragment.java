package com.example.davi.desafios2it;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.davi.desafios2it.Adapter.SongCardAdapter;
import com.example.davi.desafios2it.Util.DataSource;
import com.example.davi.desafios2it.Model.Song;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.SwipeDirection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Davi on 15/09/2017.
 */

public class MatchFragment extends Fragment {

    private View content;
    RelativeLayout matchLinearLayout;
    ProgressBar progressBar;
    private CardStackView cardStackView;
    SongCardAdapter adapter;

    DataSource source;
    List<Song> songs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        content = inflater.inflate(R.layout.fragment_match, container, false);

        source = new DataSource(getContext());

        setupView();
        reloadData();

        return content;
    }

    private void setupView(){
        matchLinearLayout = (RelativeLayout) content.findViewById(R.id.match_linear_layout);
        progressBar = (ProgressBar) content.findViewById(R.id.progressBar);
        cardStackView = (CardStackView) content.findViewById(R.id.card_stack_view);
        cardStackView.setCardEventListener(new CardStackView.CardEventListener() {
            @Override
            public void onCardDragging(float percentX, float percentY) {
                Log.d("CardStackView", "onCardDragging");
            }

            @Override
            public void onCardSwiped(SwipeDirection direction) {
                if (direction == SwipeDirection.Right) {
                    source.insertLike(songs.get(cardStackView.getTopIndex()-1));
                }
            }

            @Override
            public void onCardReversed() {
            }

            @Override
            public void onCardMovedToOrigin() {
            }

            @Override
            public void onCardClicked(int index) {
            }
        });

        FloatingActionButton dislikeButton = (FloatingActionButton) content.findViewById(R.id.dislike_floating_button);
        dislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeLeft();
            }
        });

        FloatingActionButton likeButton = (FloatingActionButton) content.findViewById(R.id.like_floating_button);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRight();
            }
        });
    }

    private void reloadData(){


        AsyncTask downloadAsync = new AsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
                matchLinearLayout.setVisibility(View.GONE);
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                String url = (String) objects[0];
                List<Song> songs = source.getSongs(url);
                return songs;
            }

            @Override
            protected void onPostExecute(Object result) {
                List<Song> songsResult = (List<Song>) result;
                adapter = new SongCardAdapter(content.getContext());
                songs = songsResult;
                adapter.addAll(songs);
                cardStackView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                matchLinearLayout.setVisibility(View.VISIBLE);
            }

        };

        downloadAsync.execute("https://itunes.apple.com/search?term=rock");


    }

    private LinkedList<Song> extractRemainingSongs() {
        LinkedList<Song> songs = new LinkedList<>();
        for (int i = cardStackView.getTopIndex(); i < adapter.getCount(); i++) {
            songs.add(adapter.getItem(i));
        }
        return songs;
    }

    private void swipeLeft(){
        LinkedList<Song> songs = extractRemainingSongs();
        if (songs.isEmpty()) {
            return;
        }

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", -10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, -2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Left, set);
    }

    private void swipeRight(){
        List<Song> songs = extractRemainingSongs();
        if (songs.isEmpty()) {
            return;
        }

        source.insertLike(songs.get(0));

        View target = cardStackView.getTopView();

        ValueAnimator rotation = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("rotation", 10f));
        rotation.setDuration(200);
        ValueAnimator translateX = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationX", 0f, 2000f));
        ValueAnimator translateY = ObjectAnimator.ofPropertyValuesHolder(
                target, PropertyValuesHolder.ofFloat("translationY", 0f, 500f));
        translateX.setStartDelay(100);
        translateY.setStartDelay(100);
        translateX.setDuration(500);
        translateY.setDuration(500);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(rotation, translateX, translateY);

        cardStackView.swipe(SwipeDirection.Right, set);
    }

}
