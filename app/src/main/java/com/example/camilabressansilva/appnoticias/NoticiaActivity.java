
package com.example.camilabressansilva.appnoticias;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticiaActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Noticia>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    /** URL das noticas do Guardian */
    private static final String NOTICIA_REQUEST_URL =
            "https://content.guardianapis.com/search?q=sustainability";

    private static final int NOTICIA_LOADER_ID = 1;

    private NoticiaAdapter mAdapter;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticia_activity);

        ListView noticiaListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        noticiaListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NoticiaAdapter(this, new ArrayList<Noticia>());

        noticiaListView.setAdapter(mAdapter);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        noticiaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                com.example.camilabressansilva.appnoticias.Noticia currentNoticia = mAdapter.getItem(position);

                Uri noticiaUri = Uri.parse(currentNoticia.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, noticiaUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NOTICIA_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.sem_internet);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_min_qtde_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            mAdapter.clear();

            mEmptyStateTextView.setVisibility(View.GONE);

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.VISIBLE);

            getLoaderManager().restartLoader(NOTICIA_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<com.example.camilabressansilva.appnoticias.Noticia>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minQtde = sharedPrefs.getString(
                getString(R.string.settings_min_qtde_key),
                getString(R.string.settings_min_qtde_default));

        String minSecao = sharedPrefs.getString(
                getString(R.string.settings_min_secao_key),
                getString(R.string.settings_min_secao_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(NOTICIA_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", "test");
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        if(minQtde.equals("")){
            uriBuilder.appendQueryParameter("page-size", "30");
        }else{
            uriBuilder.appendQueryParameter("page-size", minQtde);
        }

        uriBuilder.appendQueryParameter("order-by", orderBy);


        if(!minSecao.equals(getString(R.string.settings_min_secao_default))){
            uriBuilder.appendQueryParameter("section", minSecao.toLowerCase());
        }

        return new NoticiaLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<com.example.camilabressansilva.appnoticias.Noticia>> loader, List<com.example.camilabressansilva.appnoticias.Noticia> noticias) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.sem_noticias);

        if (noticias != null && !noticias.isEmpty()) {
            mAdapter.addAll(noticias);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<com.example.camilabressansilva.appnoticias.Noticia>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
