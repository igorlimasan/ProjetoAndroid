package com.projeto.engsoft.projetoandroid;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] navTitles;
    private final Integer[] navImage;
    public CustomList(Activity context,
                      String[] navTitles, Integer[] navImage) {
        super(context, R.layout.item_navigation, navTitles);
        this.context = context;
        this.navTitles = navTitles;
        this.navImage = navImage;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.item_navigation, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textoNav);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageNav);
        txtTitle.setText(navTitles[position]);

        imageView.setImageResource(navImage[position]);
        return rowView;
    }
}