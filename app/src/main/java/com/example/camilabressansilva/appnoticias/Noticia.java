
package com.example.camilabressansilva.appnoticias;

public class Noticia {

    private String mTitulo;

    private String mSecao;

    private String mAutor;

    private String mData;

    private String mUrl;


    public Noticia(String mTitulo, String mAutor, String mSecao, String mData, String mUrl) {
        this.mTitulo = mTitulo;
        this.mAutor = mAutor;
        this.mSecao = mSecao;
        this.mData = mData;
        this.mUrl = mUrl;
    }


    public String getUrl() {
        return mUrl;
    }

    public String getSecao() {
        return mSecao;
    }

    public String getTitulo() {
        return mTitulo;
    }

    public String getAutor() { return mAutor; }

    public String getData() {
        return mData;
    }
}
