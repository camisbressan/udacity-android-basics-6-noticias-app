
package com.example.camilabressansilva.appnoticias;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoticiaAdapter extends ArrayAdapter<Noticia> {


    public NoticiaAdapter(Context context, List<Noticia> noticias) {
        super(context, 0, noticias);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.noticia_list_item, parent, false);
        }

        Noticia currentNoticia = getItem(position);

        TextView tituloView = (TextView) listItemView.findViewById(R.id.titulo);
        tituloView.setText(currentNoticia.getTitulo());

        if(currentNoticia.getAutor().equals("")){
            TextView autorView = (TextView) listItemView.findViewById(R.id.autor);
            autorView.setVisibility(View.GONE);
        }else {
            TextView autorView = (TextView) listItemView.findViewById(R.id.autor);
            autorView.setText(currentNoticia.getAutor());
        }

        TextView secaoView = (TextView) listItemView.findViewById(R.id.secao);
        secaoView.setText(currentNoticia.getSecao());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String date = currentNoticia.getData().substring(0, 10);
        dateView.setText(date);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String time = currentNoticia.getData().substring(11, 16);
        timeView.setText(time);

        return listItemView;
    }
}
