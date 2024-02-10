package com.yong.Student;

public class Note {
    String key, title, content;

    public Note(){

    }

    public String getTitle(){
        return title;
    }

    public String getKey(){
        return key;
    }
    public String getContent(){
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
