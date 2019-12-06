
package com.example.camilabressansilva.appnoticias;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Noticia> fetchNoticiaData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Noticia> noticias = extractFeatureFromJson(jsonResponse);

        return noticias;
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the noticia JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Noticia> extractFeatureFromJson(String noticiaJSON) {
        if (TextUtils.isEmpty(noticiaJSON)) {
            return null;
        }

        List<Noticia> noticias = new ArrayList<>();

        try {

            JSONObject jObj = new JSONObject(noticiaJSON);
            JSONObject response = (JSONObject) jObj.get("response");
            JSONArray noticiaArray = (JSONArray) response.get("results");

            for (int i = 0; i < noticiaArray.length(); i++) {

                JSONObject currentNoticia = noticiaArray.getJSONObject(i);

                String titulo = currentNoticia.getString("webTitle");

                String secao = currentNoticia.getString("sectionName");

                String data = currentNoticia.getString("webPublicationDate");

                String url = currentNoticia.getString("webUrl");

                String autor = "";

                JSONArray autorArray = (JSONArray) currentNoticia.get("tags");

                for (int k = 0; k < autorArray.length(); k++) {

                    JSONObject currentAutor = autorArray.getJSONObject(k);

                    String nome = "";
                    String sobrenome = "";

                    if (currentAutor.has("lastName")){
                        nome = currentAutor.getString("lastName");
                    }
                    if (currentAutor.has("firstName")){
                        sobrenome = currentAutor.getString("firstName");
                    }

                    autor = nome + " " + sobrenome;
                }

                Noticia noticia = new Noticia(titulo, autor, secao, data, url);

                noticias.add(noticia);
            }

        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the noticia JSON results", e);
        }

        return noticias;
    }

}
