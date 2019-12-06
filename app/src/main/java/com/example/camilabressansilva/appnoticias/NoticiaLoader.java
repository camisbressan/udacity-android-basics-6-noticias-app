
package com.example.camilabressansilva.appnoticias;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NoticiaLoader extends AsyncTaskLoader<List<com.example.camilabressansilva.appnoticias.Noticia>> {

    private static final String LOG_TAG = NoticiaLoader.class.getName();

    private String mUrl;

    public NoticiaLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public List<Noticia> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Noticia> noticias = QueryUtils.fetchNoticiaData(mUrl);
        return noticias;
    }
}
