package com.example.lab02.model;

import java.io.Serializable;

public class DemoSerializableNote implements Serializable {

    private static final long serialVersionUID = 1L;

    private String title;
    private String content;

    public DemoSerializableNote() {
    }

    public DemoSerializableNote(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "DemoSerializableNote{title='" + title + "', content='" + content + "'}";
    }
}
