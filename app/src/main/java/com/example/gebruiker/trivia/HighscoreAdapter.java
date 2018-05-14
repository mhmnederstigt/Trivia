package com.example.gebruiker.trivia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.media.CamcorderProfile.get;

public class HighscoreAdapter extends ArrayAdapter<Highscore> {
    private ArrayList<Highscore> highscoreList;

    public HighscoreAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Highscore> objects) {
        super(context, resource, objects);

        highscoreList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_highscore, parent, false);
        }

        // display info per list item according to item_categories.xml

        TextView name = convertView.findViewById(R.id.name);
        name.setText(highscoreList.get(position).getName());

        TextView score = convertView.findViewById(R.id.score);
        score.setText(String.valueOf(highscoreList.get(position).getScore()));







        return convertView;

    }
}
