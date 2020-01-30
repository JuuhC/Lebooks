package com.carrati.lebooks.Model;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class Book {

    private String title;
    private String writer;
    private String thumb;
    private String price;

    public Book() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumbURL(){ return thumb; }

    public String getPrice() { return price; }

    public void setPrice(String price) {
        this.price = price;
    }
}
